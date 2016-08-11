package com.example.roberto.thefinderandroid.API;

/**
 *The ErrorResponse class holds a error message
 *
 * @author: Roberto Aguilar
 */
public class ErrorResponse {
    public String errorType;

    public ErrorResponse(String error){
        errorType = error;
    }
}
