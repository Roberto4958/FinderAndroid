package com.example.roberto.thefinderandroid.DataModel;

/**
 *The User class holds user information such as User name, first name, last name, authentication token, and ID.
 *
 * @author: Roberto Aguilar
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
