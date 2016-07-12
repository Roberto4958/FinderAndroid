package com.example.roberto.thefinderandroid;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.roberto.thefinderandroid.CustomAdapter.CustomAdapter;
import com.example.roberto.thefinderandroid.CustomAdapter.StringSetAdapter;
import com.example.roberto.thefinderandroid.CustomDiologes.HistoryDialog;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class History extends AppCompatActivity {


    private SharedPreferences sharedpreferences;
    private HistoryDialog myDiolog;
    private TextView place;
    private ArrayList<String> locations;
    private String currentClicked;
    private ListView myList;
    private StringSetAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        Set collection = sharedpreferences.getStringSet("Locations", null);
        if(collection == null) return;
        locations = new ArrayList<String>((Set) collection);
        locations = sort(locations);
        myList =(ListView) findViewById(R.id.listView);
        myAdapter = new StringSetAdapter(this, locations);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentClicked = ((String) parent.getItemAtPosition(position));
                StoreInformation(currentClicked);
                FragmentManager manager = getFragmentManager();
                myDiolog = new HistoryDialog();
                String[] Location = currentClicked.split(" ");
                String nameOfPlace = Location[3];
                for(int i=4; i< Location.length; i++){
                    nameOfPlace += " "+Location[i];
                }
                myDiolog.onCreate(nameOfPlace);
                myDiolog.show(manager, nameOfPlace);
            }
        });
    }

    public void StoreInformation(String loc){

        String[] Location = loc.split(" ");
        Double lat = Double.parseDouble((String)Location[1]);
        Double lon = Double.parseDouble((String)Location[2]);
        int id = Integer.parseInt((String)Location[0]);
        String nameOfPlace =Location[3];
        for(int i=4; i< Location.length; i++){
            nameOfPlace += " "+Location[i];
        }
        SharedPreferences preferences = getSharedPreferences("CurrentLocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong("logitude", Double.doubleToRawLongBits(lon));
        editor.putLong("latitude", Double.doubleToRawLongBits(lat));
        editor.putInt("LocationID", id);
        editor.putString("place", nameOfPlace);
        editor.commit();
    }

    public void onOpenMapClick(View v){
        myDiolog.dismiss();
        Intent intent = new Intent("com.example.roberto.thefinderandroid.MapsActivity");
        startActivity(intent);
    }

    public void onDeleteClick(View v){

        remove(currentClicked);
        myAdapter = new StringSetAdapter(this, locations);
        myList.setAdapter(myAdapter);
        myDiolog.dismiss();
    }

    public void remove(String currentRow){
        Set collection = sharedpreferences.getStringSet("Locations", null);
        for(int i=0; i < locations.size(); i++) {
            int b = Integer.parseInt((String)locations.get(i).substring(0, 1));
            if( currentRow.equals(locations.get(i))) {
                locations.remove(i);
                collection.remove(currentRow);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putStringSet("Locations", collection);
                editor.commit();
                return;
            }
        }
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

}
