package com.example.roberto.thefinderandroid;

import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.API.UserResponse;
import com.example.roberto.thefinderandroid.DataModel.*;
import com.example.roberto.thefinderandroid.DataModel.User;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener, UserResponse.UserResponseCommunicator {

    private Button signUp;
    private EditText user, pass, firstName, lastName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        user = (EditText) findViewById(R.id.userName);
        pass = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public boolean checkIfValid(String word){

        if(word.contains("/")){
            Toast.makeText(getBaseContext(), "Please do not use /", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("&")){
            Toast.makeText(getBaseContext(), "Please do not use &", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("\"")){
            Toast.makeText(getBaseContext(), "Please do not use \"", Toast.LENGTH_SHORT).show();
            return false;
        }else if(word.contains(";")){
            Toast.makeText(getBaseContext(), "Please do not use ;", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("%")){
            Toast.makeText(getBaseContext(), "Please do not use %", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("√")){
            Toast.makeText(getBaseContext(), "Please do not use √", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("π")){
            Toast.makeText(getBaseContext(), "Please do not use π", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("∆")){
            Toast.makeText(getBaseContext(), "Please do not use ∆", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("≤")){
            Toast.makeText(getBaseContext(), "Please do not use ≤", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("≥")){
            Toast.makeText(getBaseContext(), "Please do not use ≥", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("≠")){
            Toast.makeText(getBaseContext(), "Please do not use ≠", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(word.contains("℅")){
            Toast.makeText(getBaseContext(), "Please do not use ℅", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        String userName= user.getText().toString();
        String password= pass.getText().toString();
        String FName= firstName.getText().toString();
        String LName= lastName.getText().toString();

        if(userName.length()>100 || password.length()>100||FName.length()>100|| LName.length()>100){
            Toast.makeText(getBaseContext(), "Please do not use more than 100 characters", Toast.LENGTH_SHORT).show();
        }
        else if(!(checkIfValid(userName) && checkIfValid(password) && checkIfValid(FName) && checkIfValid(LName))){
            return;
        }

        else if(userName.length()>0 && password.length()>0 && FName.length()>0 && LName.length()>0) {

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                progressBar.setVisibility(View.VISIBLE);
                APIcomm call = new APIcomm(this);
                call.createAccount(userName, password, FName, LName);
            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getBaseContext(), "Please fill out the form", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void getUserResponse(User r) {
        progressBar.setVisibility(View.INVISIBLE);
        if(r == null){
            Toast.makeText(getBaseContext(), "Sorry that user name is already taken", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("UserID", r.ID);
        editor.putString("AuthToken", r.authToken);
        editor.commit();
        Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
        startActivity(intent);
    }
}
