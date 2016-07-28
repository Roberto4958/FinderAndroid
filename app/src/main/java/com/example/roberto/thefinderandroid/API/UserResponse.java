package com.example.roberto.thefinderandroid.API;

import com.example.roberto.thefinderandroid.DataModel.User;

/**
 * Created by roberto on 7/15/16.
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