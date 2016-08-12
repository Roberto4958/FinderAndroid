package com.thefinder.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.thefinder.DataModel.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.thefinder.R;

/**
 *The MapsActivity class is responsible for the functionality of the activity_maps.
 * This Activity displays a map
 *
 * @author: Roberto Aguilar
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //@desc: Manipulates the map once available
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Gets the location that was pushed from the previous activity
        String l = getIntent().getStringExtra("Location");
        Gson gson =  new Gson();
        location = gson.fromJson(l, Location.class);

        //Sets up the map
        mMap = googleMap;
        final LatLng loc = new LatLng(location.latitude,location.longtitude);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(loc).title(location.place));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

}
