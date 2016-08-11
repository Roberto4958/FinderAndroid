package com.example.roberto.thefinderandroid.API;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.CreateAccountActivity;
import com.example.roberto.thefinderandroid.CustomDiologes.StoreLocationDialog;
import com.example.roberto.thefinderandroid.LogInActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *The APIcomm class is responsible for making a http request,
 * and converting the http response to a object and sending it to the appropriate activity.
 *
 * @author: Roberto Aguilar
 */
public class APIcomm extends AsyncTask<String, Void, String> {

    private String baseURL = "http://thefinder-1.s4c2qwepti.us-west-2.elasticbeanstalk.com/webresources";
    private String RequestType;
    private Activity activity;
    private String currentRequest;
    private LocationResponse.LocationResponseCommunicator LocationCommunicator;
    private UserResponse.UserResponseCommunicator UserCommunicator;
    private Response.ResponseCommunicator Responsecummunicator;
    private HistoryResponse.HistoryResponseCommunicator HistoryComunicator;

    public APIcomm(Activity a){
        activity = a;
    }

    //@desc: Encodes users userName and passwords and starts the logIn http request
    public void logIn(String userName, String password){
        try {
            userName = URLEncoder.encode(userName.toString(),"UTF-8");
            password = URLEncoder.encode(password.toString(),"UTF-8");
            String URL = baseURL + "/logIn/"+userName+"/"+password;
            RequestType = "POST";
            currentRequest = "logIn";
            execute(URL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // @desc: Encodes users UserName, password, first name, and last name
    // and makes a createAccount http request
    public void createAccount(String userName, String password, String firstName, String lastName){

        try {
            userName = URLEncoder.encode(userName.toString(),"UTF-8");
            password = URLEncoder.encode(password.toString(),"UTF-8");
            firstName = URLEncoder.encode(firstName.toString(),"UTF-8");
            lastName = URLEncoder.encode(lastName.toString(),"UTF-8");

            String URL = baseURL+"/createAccount/"+userName+"/"+password+"/"+firstName+"/"+lastName;
            RequestType = "PUT";
            currentRequest = "createAccount";
            execute(URL);
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(activity.getBaseContext(), "Please try adding different input", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //@desc: Encodes user place, and makes a addNewLocation http request
    public void addLocation(int userID, double latitude, double longitude, String place, String auth){
        try {
            place = URLEncoder.encode(place.toString(),"UTF-8");
            String URL = baseURL+"/addNewLocation/"+place+"/"+latitude+"/"+longitude+"/"+userID+"/"+auth;
            RequestType = "PUT";
            currentRequest = "addLocation";
            execute(URL);
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(activity.getBaseContext(), "Please try a different name", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //@desc: Makes a findLocation http request.
    public void findLocation(int userID, String auth){
        String URL = baseURL+"/findLocation/"+userID+"/"+auth;
        RequestType= "GET";
        currentRequest = "findLocation";
        execute(URL);
    }

    //@desc: Makes a deleteLocation http request
    public void deleteLocation(int userID, int locationID, String auth){
        String URL = baseURL+"/deleteLocation/"+userID+"/"+locationID+"/"+auth;
        RequestType = "DELETE";
        currentRequest = "deleteLocation";
        execute(URL);
    }
    //@desc: Makes a history http request
    public void getHistory(int userID, String auth){
        String URL = baseURL+"/history/"+userID+"/"+auth;
        RequestType = "GET";
        currentRequest = "getHistory";
        execute(URL);
    }

    //@desc: Make a logOut http request
    public void logOut(int userID, String auth){
        String URL = baseURL+"/logOut/"+userID+"/"+auth;
        RequestType = "POST";
        currentRequest = "logOut";
        execute(URL);
    }

    @Override
    protected String doInBackground(String... urls) {
            return executeURL(urls[0]);
    }

    /**
     * @desc: Makes a http request
     * @param myurl - String url needed to make the http request
     * @return response from the http request
     */
    private String executeURL(String myurl) {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(RequestType);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIS(is);
            return contentAsString;
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(APIcomm.class.getName()).log(Level.SEVERE, null, ex);
            ErrorResponse re = new ErrorResponse("malformed URL");
            Gson g = new Gson();
            String  r = g.toJson(re);
            return r;
        }
        catch (ProtocolException ex) {
            Logger.getLogger(APIcomm.class.getName()).log(Level.SEVERE, null, ex);
            ErrorResponse re = new ErrorResponse("Protocal");
            Gson g = new Gson();
            String  r = g.toJson(re);
            return r;
        }
        catch (IOException ex) {
            Logger.getLogger(APIcomm.class.getName()).log(Level.SEVERE, null, ex);
            Gson g = new Gson();
            ErrorResponse re = new ErrorResponse("IOException");
            String  r = g.toJson(re);
            return r;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(APIcomm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * @desc: Turns the inputStream into a String
     * @param stream - http response inputStream
     * @return http String response
     */
    public String readIS(InputStream stream) throws IOException {
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

    /**
     * @desc: Converts http response into a object and returns it to the users activity
     * @param response - http response
     */
    @Override
    protected void onPostExecute(String response) {
        Gson g = new Gson();

        //Checks if there was a error in the http request process
        ErrorResponse error = g.fromJson(response, ErrorResponse.class);
        if(error.errorType != null){
            //Logs out the user
            SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            Intent intent = new Intent(activity, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            Toast.makeText(activity.getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        //checks if http request was a logIn
        if(currentRequest.equals("logIn")){
            UserResponse r = g.fromJson(response, UserResponse.class);

            if (r.status.equals("OK")){
                //sends users info to the logIn activity
                UserCommunicator = (UserResponse.UserResponseCommunicator)activity;
                UserCommunicator.getUserResponse(r.userInfo);
            }

            else if (r.status.equals("ERROR"))
            {
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                Toast.makeText(activity.getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
        //checks if http request was a createAccount
        else if(currentRequest.equals("createAccount")){
            UserResponse r = g.fromJson(response, UserResponse.class);


            if (r.status.equals("OK"))
            {
                //sends user info to the createAccount activity
                UserCommunicator =  (UserResponse.UserResponseCommunicator)activity;
                UserCommunicator.getUserResponse(r.userInfo);
            }
            else if (r.status.equals("ERROR"))
            {
                Intent intent = new Intent(activity, CreateAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                Toast.makeText(activity.getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(activity, CreateAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
        //checks if http request was a addLocation
        else if(currentRequest.equals("addLocation")){
            Response r = g.fromJson(response, Response.class);
            if (r.status.equals("OK"))
            {
                //lets the user Activity know that location was added
                Responsecummunicator = (Response.ResponseCommunicator) activity;
                ((Response.ResponseCommunicator) activity).getResponse();
            }
            else if(r.status.equals("TOKENCLEARED")){
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
            else if (r.status.equals("ERROR"))
            {
                ((StoreLocationDialog.Communicator)activity).onDiologMessege("error");
                Toast.makeText(activity.getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
        //checks if http request was a getHistory
        else if(currentRequest.equals("getHistory")){
            HistoryResponse r = g.fromJson(response, HistoryResponse.class);
            if(r.status.equals("OK")){
                //sends a ArrayList of Locations to the history activity
                HistoryComunicator = (HistoryResponse.HistoryResponseCommunicator)activity;
                HistoryComunicator.getHistoryResponse(r.UserLocations);
            }
            else if(r.status.equals("TOKENCLEARED")){
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
            else if (r.status.equals("ERROR"))
            {
                Toast.makeText(activity.getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
        //checks if http request was a deleteLocation
        else if(currentRequest.equals("deleteLocation")){
            Response r = g.fromJson(response, Response.class);
            if(r.status.equals("OK")){
                // lets the history activity know that location was deleted
                Responsecummunicator = (Response.ResponseCommunicator) activity;
                Responsecummunicator.getResponse();
            }
            else if(r.status.equals("TOKENCLEARED")){
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
            else if (r.status.equals("ERROR"))
            {
                Toast.makeText(activity.getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
        //checks if http request was a findLocation
        else if(currentRequest.equals("findLocation")){
            LocationResponse r = g.fromJson(response, LocationResponse.class);
            if(r.status.equals("OK")){
                //sends user activity users last location
                LocationCommunicator = (LocationResponse.LocationResponseCommunicator) activity;
                LocationCommunicator.getLocationResponse(r.locationInfo);
            }
            else if(r.status.equals("TOKENCLEARED")){
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
            else if (r.status.equals("ERROR"))
            {
                Toast.makeText(activity.getBaseContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences sharedpreferences = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent intent = new Intent(activity, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        }
    }
}
