package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.ResponseData.Response;
import com.example.roberto.thefinderandroid.ResponseData.UserResponse;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/21/16.
 */
public class DeleteAPICall {

    private Activity activity;

    public DeleteAPICall(Activity a){
        activity = a;
    }

    public void delete(int userID, int locationID, String auth){
        DeleteInSC SC = new DeleteInSC("DELETE");
        String url = "http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/deleteLocation/";
         SC.execute( url+userID+"/"+locationID+"/"+auth);

    }

    public class DeleteInSC extends ServerConnection{

        public Response.ResponseCommunicator communicator;

        public DeleteInSC(String r) {super(r);}

        @Override
        protected void onPostExecute(String result) {
            Log.v("Response", result);
            Gson gson = new Gson();
            Response r = gson.fromJson(result, Response.class);
            communicator = (Response.ResponseCommunicator) activity;
            communicator.getResponse(r);
        }
    }
}
