package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.ResponseData.Response;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/19/16.
 */
public class LogOutAPICall {

    public Activity activity;

    public LogOutAPICall(Activity a){

    }

    public void logOut(int id, String auth){
        LogOutSC call = new LogOutSC("POST");
        call.execute("http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/logOut/"+id+"/"+auth);
    }

    public class LogOutSC extends ServerConnection{

        public Response.ResponseCommunicator communicator;

        public LogOutSC(String r) {super(r);}

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            Response r = gson.fromJson(result, Response.class);
        }
    }
}
