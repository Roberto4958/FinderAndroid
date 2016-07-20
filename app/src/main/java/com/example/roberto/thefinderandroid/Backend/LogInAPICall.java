package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.ResponseData.UserResponse;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/19/16.
 */
public class LogInAPICall {

    public Activity activity;

    public LogInAPICall(Activity a){
        activity = a;
    }

    public void logIn(String userName, String password){
        LogInSC call = new LogInSC("POST");
        call.execute("http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/logIn/"+userName+"/"+password);
    }

    public class LogInSC extends ServerConnection{
        public UserResponse.UserResponseCommunicator communicator;

        public LogInSC(String r) {super(r);}

        @Override
        protected void onPostExecute(String result) {
            Log.v("Response", result);
            Gson gson = new Gson();
            UserResponse r = gson.fromJson(result, UserResponse.class);
            communicator = (UserResponse.UserResponseCommunicator) activity;
            communicator.getUserResponse(r);
        }
    }
}
