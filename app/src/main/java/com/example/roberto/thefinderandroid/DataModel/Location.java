package com.example.roberto.thefinderandroid.DataModel;

/**
 * Created by roberto on 7/6/16.
 */
public class Location {
    public double latitude;
    public double longtitude;
    public int locationID;
    public String place;

    public Location(String p, double lat, double longt, int lID){
        place = p;
        latitude = lat;
        longtitude = longt;
        locationID = lID;
    }
}
