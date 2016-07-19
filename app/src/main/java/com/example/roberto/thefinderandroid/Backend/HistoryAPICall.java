package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.History;
import com.example.roberto.thefinderandroid.ResponseData.HistoryResponse;
import com.example.roberto.thefinderandroid.ResponseData.LocationResponse;
import com.google.gson.Gson;

/**
 * Created by roberto on 7/18/16.
 */
public class HistoryAPICall {
    Activity activity;

    public HistoryAPICall(Activity a){
        activity = a;
    }

    public void getHistory(int id, String auth){
        HistorySC data = new HistorySC();
        data.execute("http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/history/"+id+"/"+auth);
    }

    public class HistorySC extends ServerConnection{

        public HistoryResponseCommunicator communicator;

        @Override
        protected void onPostExecute(String result) {
            String jsonResponse = result.substring(0, result.indexOf("OK\"}") + 4);
            Gson gson = new Gson();
            HistoryResponse r = gson.fromJson(jsonResponse, HistoryResponse.class);
            communicator = (ServerConnection.HistoryResponseCommunicator) activity;
            communicator.getHistoryResponse(r);
        }
    }
}
