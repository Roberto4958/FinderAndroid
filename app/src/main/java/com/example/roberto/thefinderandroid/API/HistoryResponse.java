package com.example.roberto.thefinderandroid.API;

import com.example.roberto.thefinderandroid.DataModel.Location;
import java.util.ArrayList;

/**
 * Created by roberto on 7/15/16.
 */
public class HistoryResponse extends Response{

    public ArrayList<Location> UserLocations;

    public HistoryResponse(ArrayList<Location> l, String s){
        super(s);
        UserLocations = l;
    }
    public interface HistoryResponseCommunicator{
        public void getHistoryResponse(ArrayList<Location> r);
    }
}
