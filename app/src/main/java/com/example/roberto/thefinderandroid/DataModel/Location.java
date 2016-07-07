package com.example.roberto.thefinderandroid.DataModel;

/**
 * Created by roberto on 7/6/16.
 */
public class Location {

    public double latitude;
    public double longitude;
    public String place;
    public int ID;

    public Location(int id, double latitude, double longitude, String place){
        this.latitude = latitude;
        this.longitude =longitude;
        this.place = place;
        ID = id;
    }
}
