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
import android.content.pm.PackageManager;
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
import android.support.v4.content.ContextCompat;
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

    public boolean checkIfValid(String word){

        if(word.contains("/")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use /", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("&")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use &", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("\"")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use \"", Toast.LENGTH_SHORT).show();
            return false;
        }else if(word.contains(";")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use ;", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("%")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use %", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(word.contains("√")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use √", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("π")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use π", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("∆")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use ∆", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("≤")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use ≤", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("≥")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use ≥", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("≠")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use ≠", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("℅")){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use ℅", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        String location = place.getText().toString();

        if(!checkIfValid(location)) return;

        else if(location.length()>40){
            Toast.makeText(((Activity)communicator).getBaseContext(), "Please do not use more than 40 characters", Toast.LENGTH_SHORT).show();
        }
        else if(location.length()>0){
            place.setText("");
            dismiss();
            userLocation = location;
            flag = displayGpsStatus();
            if (flag) {
                locationListener = new MyLocationListener();
                communicator.onDiologMessege("starting");
                try{
                    locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                }
                catch (Exception e){
                    Toast.makeText(((Activity)communicator).getBaseContext(), "Error traking your location", Toast.LENGTH_SHORT).show();
                    communicator.onDiologMessege("No permisson");
                }
            } else {
                communicator.onDiologMessege("No permisson");
            }
        }
    }

    public void StopLocationTracker(){
        locationMangaer.removeUpdates(locationListener);
    }

    private Boolean displayGpsStatus() {
        if (!locationMangaer.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            alertboxDL();
            return false;
        }
        if(ContextCompat.checkSelfPermission( ((Activity)communicator), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            alertboxDP();
            return false;
        }
        return true;
    }
    // Denied Location turned off
    protected void alertboxDL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(((Activity)communicator));
        builder.setMessage("Your device's GPS is Disable please navigate to Setting->location and click on").setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Turn On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ((Activity)communicator).startActivity(myIntent);
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
    //Denied permision
    protected void alertboxDP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(((Activity)communicator));
        builder.setMessage("Your device's denies permission for this app to access your Location, please navigate to Setting->app->TheFinderAndroid->permissions and click on Location").setCancelable(false)
                .setTitle("** Permission Status **")
                .setPositiveButton("Turn On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                                ((Activity)communicator).startActivity(myIntent);
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

        ConnectivityManager connMgr = (ConnectivityManager)((Activity)communicator).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            APIcomm call = new APIcomm(((Activity)communicator));
            call.addLocation(userID, loc.getLatitude(), loc.getLongitude(), userLocation, auth);
        }
        else{
            communicator.onDiologMessege("Not Connected");
            Toast.makeText(((Activity)communicator).getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
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
