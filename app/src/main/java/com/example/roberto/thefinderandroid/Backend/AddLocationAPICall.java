package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.ResponseData.LocationResponse;
import com.example.roberto.thefinderandroid.ResponseData.Response;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/21/16.
 */
public class AddLocationAPICall {

    private Activity activity;

    public AddLocationAPICall(Activity a){
        activity =a;
    }

    public void addLocation(int id, double latitude, double longitude, String place, String auth){
        AddLocationSC SC = new AddLocationSC("PUT");
        String url = "http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/addNewLocation/";
        url = url+place+"/"+latitude+"/"+longitude+"/"+id+"/"+auth;
        SC.execute(url);
    }

    public class AddLocationSC extends ServerConnection{

        public Response.ResponseCommunicator communicator;

        public AddLocationSC(String r) {super(r);}

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            Response r = gson.fromJson(result, Response.class);
            communicator = (Response.ResponseCommunicator)activity;
            communicator.getResponse(r);
        }
    }
}
