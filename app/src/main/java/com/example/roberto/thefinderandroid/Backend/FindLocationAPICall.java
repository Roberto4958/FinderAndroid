package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.ResponseData.LocationResponse;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/18/16.
 */
public class FindLocationAPICall {

    public Activity activity;

    public FindLocationAPICall(Activity a){
        activity = a;
    }

    public void findLastLocation(int id, String auth){

        FindLocationSC data =  new FindLocationSC("GET");
        data.execute("http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/findLocation/"+id+"/"+auth);
    }

    public class FindLocationSC extends ServerConnection{

        public LocationResponse.LocationResponseCommunicator communicator;

        public FindLocationSC(String r) {super(r);}

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            LocationResponse r = gson.fromJson(result, LocationResponse.class);
            communicator = (LocationResponse.LocationResponseCommunicator)activity;
            communicator.getLocationResponse(r);
        }

    }
}
