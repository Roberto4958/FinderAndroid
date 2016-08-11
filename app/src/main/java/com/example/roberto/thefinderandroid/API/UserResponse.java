package com.example.roberto.thefinderandroid.API;

import com.example.roberto.thefinderandroid.DataModel.User;

/**
 *The UserResponse class holds a user Object, and a status String of the value
 * "OK" or "ERROR" or "TOKENCLEARD" these values indicate the http response status.
 * Implemented a UserResponseCommunicator interface to communicate the user object from one class to another.
 *
 * @author: Roberto Aguilar
 */
public class UserResponse extends Response{

    public User userInfo;

    public UserResponse(User u, String s){
        super(s);
        userInfo = u;
    }

    public interface UserResponseCommunicator{
        public void getUserResponse(User r);
    }
}