package com.example.roberto.thefinderandroid.ResponseData;

/**
 * Created by roberto on 7/15/16.
 */
public class Response {
    public String status;

    public Response(String s){
        status = s;
    }

    public interface ResponseCommunicator{
        public void getResponse(Response r);
    }
}
