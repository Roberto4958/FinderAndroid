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
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.Backend.AddLocationAPICall;
import com.example.roberto.thefinderandroid.Backend.FindLocationAPICall;
import com.example.roberto.thefinderandroid.Backend.LogOutAPICall;
import com.example.roberto.thefinderandroid.CustomDiologes.StoreLocationDiologe;
import com.example.roberto.thefinderandroid.ResponseData.LocationResponse;
import com.example.roberto.thefinderandroid.ResponseData.Response;
import com.google.gson.Gson;

public class User extends AppCompatActivity implements StoreLocationDiologe.Communicator, Response.ResponseCommunicator,  LocationResponse.LocationResponseCommunicator {

    private SharedPreferences sharedpreferences;
    private Button findLocation;
    private TextView progress;
    private StoreLocationDiologe diologe;
    private String userLocation;
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    private FragmentManager manager;
    private int userID;
    private String auth;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        progress = (TextView)findViewById(R.id.progress);
        manager = getFragmentManager();
        diologe = new StoreLocationDiologe();
        findLocation = (Button) findViewById(R.id.findLastLocation);
        findLocation.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logOut){
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                int userID = sharedpreferences.getInt("UserID", -1);
                String auth = sharedpreferences.getString("AuthToken", null);
                LogOutAPICall call = new LogOutAPICall(this);
                call.logOut(userID, auth);
                sharedpreferences = getSharedPreferences("CurrentLocation", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        Intent intent = new Intent("com.example.roberto.thefinderandroid.History");
        startActivity(intent);

        findLocation.setVisibility(View.INVISIBLE);
        progress.setText("");
    }

    public void onFindLocationclick(View v) {

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            FindLocationAPICall call = new FindLocationAPICall(this);
            call.findLastLocation(userID, auth);
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
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

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            AddLocationAPICall call = new AddLocationAPICall(this);
            call.addLocation(userID, loc.getLatitude(), loc.getLongitude(), userLocation, auth);
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResponse(Response r) {
        if(r.status.equals("OK")){
            progress.setText("Success");
            progress.setTextColor(Color.GREEN);
            findLocation.setVisibility(View.VISIBLE);
        }
        else if(r.status.equals("TOKENCLEARED")){
            sharedpreferences = getSharedPreferences("CurrentLocation", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            progress.setText("");
            Toast.makeText(getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getLocationResponse(LocationResponse r) {

        findLocation.setVisibility(View.INVISIBLE);
        progress.setText("");

        if(r.status.equals("OK")) {
            Gson gson = new Gson();
            String loc = gson.toJson(r.result);
            Intent intent = new Intent(User.this, MapsActivity.class).putExtra("Location", loc);
            startActivity(intent);
        }
        else if(r.status.equals("TOKENCLEARED")){
            sharedpreferences = getSharedPreferences("CurrentLocation", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else Toast.makeText(getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            //Toast.makeText(getBaseContext(), " Lat: " + loc.getLatitude() + "\n Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
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
