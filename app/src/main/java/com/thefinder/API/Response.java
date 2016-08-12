package com.thefinder.API;

/**
 *The Response class holds a status String of the value
 * "OK" or "ERROR" or "TOKENCLEARD" these values indicate the http response status
 * Implemented a ResponseCommunicator interface to communicate the status from one class to another.
 *
 * @author: Roberto Aguilar
 */
public class Response {
    public String status;

    public Response(String s){
        status = s;
    }

    public interface ResponseCommunicator{
        public void getResponse();
    }
}
