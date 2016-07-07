package com.example.roberto.thefinderandroid.DataModel;

/**
 * Created by roberto on 7/6/16.
 */
public class User {
    public String userName;
    public String firstName;
    public String lastName;
    public String authToken;
    public int ID;

    public User(String user, String first, String last, String token, int id){
        userName = user;
        firstName = first;
        lastName = last;
        authToken = token;
        ID = id;
    }

}
