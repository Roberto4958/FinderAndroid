package com.example.roberto.thefinderandroid;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.CustomAdapter.HistoryAdapter;
import com.example.roberto.thefinderandroid.CustomDiologes.HistoryDialog;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.API.HistoryResponse;
import com.example.roberto.thefinderandroid.API.Response;
import java.util.ArrayList;

/**
 *The HistoryActivity class is responsible for the functionality of the activity_history.
 * This Activity displays a list of locations that the user has saved.
 *
 * @author: Roberto Aguilar
 */

public class HistoryActivity extends AppCompatActivity implements HistoryResponse.HistoryResponseCommunicator, Response.ResponseCommunicator {

    private SharedPreferences sharedpreferences;
    private HistoryDialog myDiolog;
    private ListView myList;
    private HistoryAdapter myAdapter;
    private int userID;
    private String auth;
    private ProgressBar progressBar;
    private Activity activity;

    //@desc: sets up the spinner and starts a API call to get users history
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
        activity = this;
    }

    //@desc: Adds the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //@desc: Logs out user if log out was selected
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
            //checks if user is connected to internet
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                APIcomm call = new APIcomm(this);
                call.logOut(userID, auth);

                sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(this, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @Communicator method from APIcomm: APIcomm gives a ArrayList if Location Objects
     * @param: Collection - ArrayList of users Location
     * @desc: Makes a ListView with all the users location
     */
    @Override
    public void getHistoryResponse(ArrayList<Location> collection) {
        progressBar.setVisibility(View.INVISIBLE);
        myList = (ListView) findViewById(R.id.listView);
        myAdapter = new HistoryAdapter(this, collection);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location currentClicked = ((Location) parent.getItemAtPosition(position));
                FragmentManager manager = getFragmentManager();
                myDiolog = new HistoryDialog();
                myDiolog.onCreate(currentClicked, activity); // to have a reference of the name in the diolog
                myDiolog.show(manager, "Diologe");
            }
        });
    }

    /**
     * @Communicator method from APIcomm: Lets this class know Location was successfully deleted
     * @desc: Gets history after deletion to display the left over locations
     */
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
