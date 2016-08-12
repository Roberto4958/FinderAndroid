package com.thefinder.API;

import com.thefinder.DataModel.Location;
import java.util.ArrayList;

/**
 *The HistoryResponse class holds a ArrayList of Locations, and a status String of the value
 * "OK" or "ERROR" or "TOKENCLEARD" these values indicate the http response status.
 * Implemented a LocationResponseCommunicator interface to communicate the users locations from one class to another.
 *
 * @author: Roberto Aguilar
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
