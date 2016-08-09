package com.example.roberto.thefinderandroid;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
    private TextView progressTextView;
    private StoreLocationDiologe diologe;
    private FragmentManager manager;
    private int userID;
    private String auth;
    private ProgressBar progressBar;

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
        progressTextView = (TextView)findViewById(R.id.progress);
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

        if(item.getItemId() == R.id.logOut){
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                APIcomm call = new APIcomm(this);
                call.logOut(userID, auth);

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

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = new Intent("com.example.roberto.thefinderandroid.History");
            startActivity(intent);
            findLocation.setVisibility(View.INVISIBLE);
            progressTextView.setText("");
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
        progressTextView.setText("");

    }

    public void onAddLocationClick(View view) {
        diologe.show(manager, "my diolog");
    }

    @Override
    public void onDiologMessege(String progress) {
        if(progress.equals("starting")){
            progressTextView.setText("Please move your device");
            progressTextView.setTextColor(Color.RED);
        }
        else if(progress.equals("Not Connected")){
            progressTextView.setText("");
        }
        else if(progress.equals("No permisson")){
            progressTextView.setText("");
        }
        else if(progress.equals("error")){
            progressTextView.setText("");
        }
        else if(progress.equals("try again")){
            progressTextView.setText("Please try again");
        }
    }

    @Override
    public void getResponse() {
        progressTextView.setText("Success");
        progressTextView.setTextColor(Color.GREEN);
        findLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void getLocationResponse(com.example.roberto.thefinderandroid.DataModel.Location r) {
        Gson gson = new Gson();
        String loc = gson.toJson(r);
        Intent intent =  new Intent(User.this, MapsActivity.class).putExtra("Location", loc);
        startActivity(intent);
    }
}
