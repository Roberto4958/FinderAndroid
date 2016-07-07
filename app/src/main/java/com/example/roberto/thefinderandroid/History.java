package com.example.roberto.thefinderandroid;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.roberto.thefinderandroid.CustomAdapter.CustomAdapter;
import com.example.roberto.thefinderandroid.CustomDiologes.HistoryDialog;
import com.example.roberto.thefinderandroid.DataModel.Location;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    Location l1 = new Location(1, 37.402288, -122.096560, "Pool");
    Location l2 = new Location(2, 37.390421, -122.083752, "Library");
    Location l3 = new Location(3, 37.333287, -121.879723, "SJSU");
    Location l4 = new Location(4, 37.399986, -122.067555, "Park");

    private ArrayList<Location>  locations;
    private SharedPreferences sharedpreferences;
    private HistoryDialog myDiolog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locations = new ArrayList<Location>();
        locations.add(l1);
        locations.add(l2);
        locations.add(l3);
        locations.add(l4);

        CustomAdapter myAdapter = new CustomAdapter(this, locations);
        ListView myList =(ListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreInformation((Location) parent.getItemAtPosition(position));
                FragmentManager manager = getFragmentManager();
                myDiolog = new HistoryDialog(((Location) parent.getItemAtPosition(position)).place);
                myDiolog.show(manager, "my diolog");
            }
        });

    }

    public void StoreInformation(Location location){
        sharedpreferences = getSharedPreferences("TheirLocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong("logitude", Double.doubleToRawLongBits(location.longitude));
        editor.putLong("latitude", Double.doubleToRawLongBits(location.latitude));
        editor.putInt("LocationID", location.ID);
        editor.putString("place", location.place);
        editor.commit();
    }
    public void onOpenMapClick(View v){
        myDiolog.dismiss();
        Intent intent = new Intent("com.example.roberto.thefinderandroid.MapsActivity");
        startActivity(intent);
    }
    public void onDeleteClick(View v){
        final SharedPreferences prefs = getSharedPreferences("TheirLocation", Context.MODE_PRIVATE);
        int id = prefs.getInt("LocationID", -1);
        remove(id);

        CustomAdapter myAdapter = new CustomAdapter(this, locations);
        ListView myList =(ListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter);
        myDiolog.dismiss();
    }
    public void remove(int id){
        for(int i=0; i < locations.size(); i++){
            if(locations.get(i).ID == id){
                locations.remove(i);
                return;
            }
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
