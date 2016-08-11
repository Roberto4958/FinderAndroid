package com.example.roberto.thefinderandroid.DataModel;

/**
 *The Location class holds Location information such as latitude, longitude, ID, and the name of the location.
 *
 * @author: Roberto Aguilar
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
