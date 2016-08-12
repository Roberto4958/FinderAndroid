package com.thefinder.API;

import com.thefinder.DataModel.Location;

/**
 *The LocationResponse class holds a Location, and a status String of the value
 * "OK" or "ERROR" or "TOKENCLEARD" these values indicate the http response status.
 * Implemented a LocationResponseCommunicator interface to communicate the location from one class to another.
 *
 * @author: Roberto Aguilar
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
