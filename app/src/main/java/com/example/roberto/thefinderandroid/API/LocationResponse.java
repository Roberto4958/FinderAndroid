package com.example.roberto.thefinderandroid.API;

import com.example.roberto.thefinderandroid.DataModel.Location;

/**
 * Created by roberto on 7/15/16.
 */
public class LocationResponse extends Response{
    public Location locationInfo;

    public LocationResponse(Location l, String s){

        super(s);
        locationInfo = l;
    }

    public interface LocationResponseCommunicator{
        public void getLocationResponse(Location r);
    }
}
