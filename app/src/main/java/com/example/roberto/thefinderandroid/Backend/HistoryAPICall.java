package com.example.roberto.thefinderandroid.Backend;

import android.app.Activity;
import android.util.Log;

import com.example.roberto.thefinderandroid.ResponseData.HistoryResponse;
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
        HistorySC data = new HistorySC("GET");
        data.execute("http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources/history/"+id+"/"+auth);
    }

    public class HistorySC extends ServerConnection{

        public HistoryResponse.HistoryResponseCommunicator communicator;

        public HistorySC(String r) {
            super(r);
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            HistoryResponse r = gson.fromJson(result, HistoryResponse.class);
            communicator = (HistoryResponse.HistoryResponseCommunicator) activity;
            communicator.getHistoryResponse(r);
        }
    }
}
