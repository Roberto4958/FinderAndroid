package com.example.roberto.thefinderandroid;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.*;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.API.LocationResponse;
import com.example.roberto.thefinderandroid.API.Response;
import com.example.roberto.thefinderandroid.CustomDiologes.StoreLocationDiologe;
import com.google.gson.Gson;


public class User extends AppCompatActivity implements StoreLocationDiologe.Communicator, Response.ResponseCommunicator, LocationResponse.LocationResponseCommunicator{

    private SharedPreferences sharedpreferences;
    private Button findLocation, addLocation, history;
    private TextView progress;
    private StoreLocationDiologe diologe;
    private String userLocation;
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    private FragmentManager manager;
    private int userID;
    private String auth;
    private ProgressBar progressBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addLocation = (Button)findViewById(R.id.addLocation);
        history = (Button) findViewById(R.id.history);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        findLocation = (Button) findViewById(R.id.findLastLocation);
        findLocation.setVisibility(View.INVISIBLE);
        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        progress = (TextView)findViewById(R.id.progress);
        manager = getFragmentManager();
        diologe = new StoreLocationDiologe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addLocation.setVisibility(View.VISIBLE);
        history.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);
        if(userID == -1){
            Toast.makeText(getBaseContext(), "Please try again in 5 seconds", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(item.getItemId() == R.id.logOut){
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                APIcomm call = new APIcomm(this);
                call.logOut(userID, auth);
            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void onHistoryclick(View v) {

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);
        if(userID == -1){
            Toast.makeText(getBaseContext(), "Please try again in 5 seconds", Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = new Intent("com.example.roberto.thefinderandroid.History");
            startActivity(intent);
            findLocation.setVisibility(View.INVISIBLE);
            progress.setText("");
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
    }

    public void onFindLocationclick(View v) {

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            APIcomm call = new APIcomm(this);
            call.findLocation(userID, auth);
        }
        else{
            Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
            return;
        }

        addLocation.setVisibility(View.INVISIBLE);
        history.setVisibility(View.INVISIBLE);
        findLocation.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        progress.setText("");

    }

    public void onAddLocationClick(View view) {
        diologe.show(manager, "my diolog");
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) return true;
        else return false;
    }

    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable").setCancelable(false)
                .setTitle("** Gps Status **").setPositiveButton("GPS On",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // finish the current activity
                        // AlertBoxAdvance.this.finish();
                        Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        startActivity(myIntent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDiologMessege(String place) {
        userLocation = place;
        flag = displayGpsStatus();
        if (flag) {
            locationListener = new MyLocationListener();
            progress.setText("Please move your device");
            progress.setTextColor(Color.RED);
            locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }
    }

    public void StopLocationTracker(){
        locationMangaer.removeUpdates(locationListener);
    }


    public void storeLocation(Location loc){

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);
        if(userID == -1) {
            progress.setText("Please try again");
            return;
        }
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            APIcomm call = new APIcomm(this);
            call.addLocation(userID, loc.getLatitude(), loc.getLongitude(), userLocation, auth);
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResponse() {
        progress.setText("Success");
        progress.setTextColor(Color.GREEN);
        findLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void getLocationResponse(com.example.roberto.thefinderandroid.DataModel.Location r) {
        r.place = r.place.replace("@20", " ");
        Gson gson = new Gson();
        String loc = gson.toJson(r);
        Intent intent =  new Intent(User.this, MapsActivity.class).putExtra("Location", loc);
        startActivity(intent);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            StopLocationTracker();
            storeLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}
