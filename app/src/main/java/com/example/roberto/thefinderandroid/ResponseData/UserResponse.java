package com.example.roberto.thefinderandroid.ResponseData;

import com.example.roberto.thefinderandroid.DataModel.User;

/**
 * Created by roberto on 7/15/16.
 */
public class UserResponse extends Response{

    public User results;

    public UserResponse(User u, String s){
        super(s);
        results = u;
    }

    public interface UserResponseCommunicator{
        public void getUserResponse(UserResponse r);
    }
}