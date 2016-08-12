package com.thefinder.CustomDialogs;

/**
 *The HistoryDialog class is responsible for the functionality of history_dialog.
 * This dialog displays when a location in is clicked in the history activity.
 * This dialog gives the user the option to either delete or open maps to the location clicked.
 *
 * @author: Roberto Aguilar
 */
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thefinder.API.APIcomm;
import com.thefinder.DataModel.Location;
import com.thefinder.Activities.MapsActivity;
import com.thefinder.R;
import com.google.gson.Gson;


public class HistoryDialog extends DialogFragment implements View.OnClickListener{

    private TextView place;
    private View view;
    private Location Location;
    private Activity activity;
    private Button DeleteLocation, openMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.history_dialog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        openMap = (Button) view.findViewById(R.id.openMap);
        DeleteLocation = (Button) view.findViewById(R.id.DeleteLocation);
        place = (TextView)view.findViewById(R.id.place);
        place.setText(Location.place);
        openMap.setOnClickListener((View.OnClickListener) this);
        DeleteLocation.setOnClickListener((View.OnClickListener) this);
        return view;
    }

    //@desc: store a reference history activity and location clicked
    public void onCreate(com.thefinder.DataModel.Location location, Activity a) {
        Location = location;
        activity = a;
    }

    @Override
    public void onClick(View view) {
        //if delete clicked make a API call to delete location from the database
        if(view.getId() == R.id.DeleteLocation){
            SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
            int userID = sharedpreferences.getInt("UserID", -1);
            String auth = sharedpreferences.getString("AuthToken", null);
            //checks if user is connected to internet
            ConnectivityManager connMgr = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                APIcomm call = new APIcomm(activity);
                call.deleteLocation(userID,Location.locationID, auth);
                dismiss();
            }
            else Toast.makeText(activity.getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
        //if open clicked makes current location to a json String and sends String to the mapActivity
        else if(view.getId() == R.id.openMap){
            Gson gson = new Gson();
            String loc = gson.toJson(Location);
            Intent intent =  new Intent(activity, MapsActivity.class).putExtra("Location", loc);
            startActivity(intent);
            dismiss();
        }
    }
}
