package com.example.roberto.thefinderandroid.CustomDiologes;

/**
 * Created by roberto on 7/6/16.
 */
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.DataModel.Location;
import com.example.roberto.thefinderandroid.MapsActivity;
import com.example.roberto.thefinderandroid.R;
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

        view = inflater.inflate(R.layout.history_diolog, null);
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

    public void onCreate(com.example.roberto.thefinderandroid.DataModel.Location location, Activity a) {
        Location = location;
        activity = a;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.DeleteLocation){
            SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
            int userID = sharedpreferences.getInt("UserID", -1);
            String auth = sharedpreferences.getString("AuthToken", null);

            ConnectivityManager connMgr = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                APIcomm call = new APIcomm(activity);
                call.deleteLocation(userID,Location.locationID, auth);
                dismiss();
            }
            else Toast.makeText(activity.getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.openMap){
            Gson gson = new Gson();
            String loc = gson.toJson(Location);
            Intent intent =  new Intent(activity, MapsActivity.class).putExtra("Location", loc);
            startActivity(intent);
            dismiss();
        }
    }
}
