package com.example.roberto.thefinderandroid;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.CustomAdapter.CustomAdapter;
import com.example.roberto.thefinderandroid.CustomDiologes.HistoryDialog;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.API.HistoryResponse;
import com.example.roberto.thefinderandroid.API.Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;


public class History extends AppCompatActivity implements HistoryResponse.HistoryResponseCommunicator, Response.ResponseCommunicator {


    private SharedPreferences sharedpreferences;
    private HistoryDialog myDiolog;
    private TextView place;
    private ArrayList<String> locations;
    private Location currentClicked;
    private ListView myList;
    private CustomAdapter myAdapter;
    private int userID;
    private String auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        APIcomm call = new APIcomm(this);
        call.getHistory(userID, auth);
    }

    public void onOpenMapClick(View v){
        myDiolog.dismiss();
        Gson gson = new Gson();
        String loc = gson.toJson(currentClicked);
        Intent intent =  new Intent(History.this, MapsActivity.class).putExtra("Location", loc);
        startActivity(intent);
    }

    public void onDeleteClick(View v){
        myDiolog.dismiss();

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            APIcomm call = new APIcomm(this);
            call.deleteLocation(userID,currentClicked.locationID, auth);
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
    public void getHistoryResponse(ArrayList<Location> collection) {
        progressBar.setVisibility(View.INVISIBLE);
        if (collection.size() < 1) return;
        myList = (ListView) findViewById(R.id.listView);
        myAdapter = new CustomAdapter(this, collection);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentClicked = ((Location) parent.getItemAtPosition(position));
                FragmentManager manager = getFragmentManager();
                myDiolog = new HistoryDialog();
                String nameOfPlace = ((Location) parent.getItemAtPosition(position)).place;
                myDiolog.onCreate(nameOfPlace); // to have a refrence of the name in the diologe
                myDiolog.show(manager, nameOfPlace);
            }
        });
    }

    @Override
    public void getResponse() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            APIcomm call = new APIcomm(this);
            call.getHistory(userID, auth);
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
    }
}
