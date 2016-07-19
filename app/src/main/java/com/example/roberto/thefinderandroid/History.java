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

import com.example.roberto.thefinderandroid.Backend.HistoryAPICall;
import com.example.roberto.thefinderandroid.Backend.ServerConnection;
import com.example.roberto.thefinderandroid.CustomAdapter.CustomAdapter;
import com.example.roberto.thefinderandroid.CustomDiologes.HistoryDialog;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.ResponseData.HistoryResponse;
import java.util.ArrayList;


public class History extends AppCompatActivity implements ServerConnection.HistoryResponseCommunicator {


    private SharedPreferences sharedpreferences;
    private HistoryDialog myDiolog;
    private TextView place;
    private ArrayList<String> locations;
    private Location currentClicked;
    private ListView myList;
    private CustomAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        int userID = sharedpreferences.getInt("UserID", -1);
        String auth = sharedpreferences.getString("AuthToken", null);

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
        Intent intent = new Intent("com.example.roberto.thefinderandroid.MapsActivity");
        startActivity(intent);
    }

    public void onDeleteClick(View v){
        Toast.makeText(getBaseContext(), "Sorry this is not Supported", Toast.LENGTH_SHORT).show();
    }


    public ArrayList<String> sort(ArrayList<String> loc){

        for(int i =1; i < loc.size(); i++){
            String id = loc.get(i-1).substring(0, 1);
            int MaxID = Integer.parseInt(id);
            String ID = loc.get(i).substring(0, 1);
            int currentID = Integer.parseInt(ID);
            int b=i;

            while(MaxID < currentID && b>0){

                String save = loc.get(b);
                loc.set(b, loc.get(b-1));
                loc.set(b-1, save);
                b--;
                if(b>0) {
                    MaxID = Integer.parseInt(loc.get(b - 1).substring(0, 1));
                    currentID = Integer.parseInt(loc.get(b).substring(0, 1));
                }
            }
        }
        return loc;
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

            sharedpreferences = getSharedPreferences("Location", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getHistoryResponse(HistoryResponse r) {
        ArrayList<Location> collection = r.result;
        if(collection == null) return;
        //locations = sort(locations);
        myList =(ListView) findViewById(R.id.listView);
        myAdapter = new CustomAdapter(this, collection);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentClicked = ((Location) parent.getItemAtPosition(position));
                StoreInformation(currentClicked);
                FragmentManager manager = getFragmentManager();
                myDiolog = new HistoryDialog();
                String nameOfPlace =((Location) parent.getItemAtPosition(position)).place;
                myDiolog.onCreate(nameOfPlace); // to have a refrence of the name in the diologe
                myDiolog.show(manager, nameOfPlace);
            }
        });
    }
}
