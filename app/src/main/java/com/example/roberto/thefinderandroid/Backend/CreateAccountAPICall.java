package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.ResponseData.UserResponse;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/19/16.
 */
public class CreateAccountAPICall {

    public Activity activity;

    public CreateAccountAPICall(Activity a){
        activity = a;
    }

    public void MakeAccount(String userName, String password, String firstName, String lastName){
        CreateAccountSC call = new CreateAccountSC("PUT");
        call.execute("http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/createAccount/"+userName+"/"+password+"/"+firstName+"/"+lastName);
    }

    public class CreateAccountSC extends ServerConnection{
        public UserResponse.UserResponseCommunicator communicator;

        public CreateAccountSC(String r) {super(r);}

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            UserResponse r = gson.fromJson(result, UserResponse.class);
            communicator = (UserResponse.UserResponseCommunicator) activity;
            communicator.getUserResponse(r);
        }
    }
}
