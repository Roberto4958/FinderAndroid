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
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.Backend.DeleteAPICall;
import com.example.roberto.thefinderandroid.Backend.HistoryAPICall;
import com.example.roberto.thefinderandroid.Backend.LogOutAPICall;
import com.example.roberto.thefinderandroid.CustomAdapter.CustomAdapter;
import com.example.roberto.thefinderandroid.CustomDiologes.HistoryDialog;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.ResponseData.HistoryResponse;
import com.example.roberto.thefinderandroid.ResponseData.Response;
import com.example.roberto.thefinderandroid.ResponseData.UserResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("UserID", -1);
        auth = sharedpreferences.getString("AuthToken", null);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            HistoryAPICall call = new HistoryAPICall(this);
            call.getHistory(userID, auth);
        }
        else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
    }

    public void StoreInformation(Location loc){
        sharedpreferences = getSharedPreferences("CurrentLocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong("logitude", Double.doubleToRawLongBits(loc.longtitude));
        editor.putLong("latitude", Double.doubleToRawLongBits(loc.latitude));
        editor.putInt("LocationID", loc.locationID);
        editor.putString("place", loc.place);
        editor.commit();
    }

    public void onOpenMapClick(View v){
        myDiolog.dismiss();
        Gson gson = new Gson();
        String loc = gson.toJson(currentClicked);
        Intent intent =  new Intent(History.this, MapsActivity.class).putExtra("Location", loc);
        startActivity(intent);
    }

    public void onDeleteClick(View v)throws IOException{
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DeleteAPICall call = new DeleteAPICall(this);
            call.delete(userID, currentClicked.locationID ,auth);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logOut){
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

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
    public void getHistoryResponse(HistoryResponse r) {
        if(r.status.equals("OK")) {
            ArrayList<Location> collection = r.result;
            if (collection.size() < 1) return;
            myList = (ListView) findViewById(R.id.listView);
            myAdapter = new CustomAdapter(this, collection);
            myList.setAdapter(myAdapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentClicked = ((Location) parent.getItemAtPosition(position));
                    StoreInformation(currentClicked);
                    FragmentManager manager = getFragmentManager();
                    myDiolog = new HistoryDialog();
                    String nameOfPlace = ((Location) parent.getItemAtPosition(position)).place;
                    myDiolog.onCreate(nameOfPlace); // to have a refrence of the name in the diologe
                    myDiolog.show(manager, nameOfPlace);
                }
            });
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
            Toast.makeText(getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void getResponse(Response r) {
        if(r.status.equals("OK")){

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                HistoryAPICall call = new HistoryAPICall(this);
                call.getHistory(userID, auth);
            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
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

        myDiolog.dismiss();
    }
}
