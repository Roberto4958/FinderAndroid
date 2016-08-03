package com.example.roberto.thefinderandroid.CustomDiologes;

/**
 * Created by roberto on 7/6/16.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.R;


public class StoreLocationDiologe extends DialogFragment implements View.OnClickListener {

    private Button save;
    private EditText place;
    private Communicator communicator;
    private String userLocation;
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator =(Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.add_location_diolog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener((View.OnClickListener) this);
        place = (EditText) view.findViewById(R.id.locationName);
        locationMangaer = (LocationManager) ((Activity)communicator).getSystemService(Context.LOCATION_SERVICE);
        return view;
    }

    public void showDialog(View view){
        FragmentManager manager = getFragmentManager();
    }

    @Override
    public void onClick(View view) {
        String location = place.getText().toString();
        if(location.contains("/")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use /", Toast.LENGTH_SHORT).show();
        }
        else if(location.contains("&")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use &", Toast.LENGTH_SHORT).show();
        }
        else if(location.length()>0){
            place.setText("");
            dismiss();
            userLocation = location;
            flag = displayGpsStatus();
            if (flag) {
                locationListener = new MyLocationListener();
                communicator.onDiologMessege("starting");
                locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            } else {
                alertbox("Gps Status!!", "Your GPS is: OFF");
            }
        }
    }

    public void StopLocationTracker(){
        locationMangaer.removeUpdates(locationListener);
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = ((Activity)communicator).getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) return true;
        else return false;
    }

    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(((Activity)communicator));
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

    public void storeLocation(Location loc){

        SharedPreferences sharedpreferences = ((Activity)communicator).getSharedPreferences("User", Context.MODE_PRIVATE);
        int userID = sharedpreferences.getInt("UserID", -1);
        String auth = sharedpreferences.getString("AuthToken", null);
        if(userID == -1) {
            communicator.onDiologMessege("try again");
            return;
        }
        ConnectivityManager connMgr = (ConnectivityManager)((Activity)communicator).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            APIcomm call = new APIcomm(((Activity)communicator));
            call.addLocation(userID, loc.getLatitude(), loc.getLongitude(), userLocation, auth);
        }
        else Toast.makeText(((Activity)communicator).getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
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

    public interface Communicator{
        public void onDiologMessege(String place);
    }
}
