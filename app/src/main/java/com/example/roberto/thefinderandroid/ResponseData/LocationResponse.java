package com.example.roberto.thefinderandroid.ResponseData;

import com.example.roberto.thefinderandroid.DataModel.Location;

/**
 * Created by roberto on 7/15/16.
 */
public class LocationResponse extends Response{
    public Location result;

    public LocationResponse(Location l, String s){

        super(s);
        result = l;
    }

    public interface LocationResponseCommunicator{
        public void getLocationResponse(LocationResponse r);
    }
}
