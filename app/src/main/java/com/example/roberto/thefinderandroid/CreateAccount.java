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
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.API.UserResponse;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private Button signUp;
    private EditText user, pass, firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user = (EditText) findViewById(R.id.userName);
        pass = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String userName= user.getText().toString();
        String password= pass.getText().toString();
        String FName= firstName.getText().toString();
        String LName= lastName.getText().toString();

        if(userName.contains("/") || password.contains("/")|| FName.contains("/") || LName.contains("/")){
            Toast.makeText(getBaseContext(), "Please do not use /", Toast.LENGTH_SHORT).show();
        }

        else if(userName.contains("&") || password.contains("&")|| FName.contains("&") || LName.contains("&")){
            Toast.makeText(getBaseContext(), "Please do not use &", Toast.LENGTH_SHORT).show();
        }

        else if(userName.length()>0 && password.length()>0 && FName.length()>0 && LName.length()>0) {

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
                APIcomm call = new APIcomm(this);
                call.createAccount(userName, password, FName, LName);
                startActivity(intent);

            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getBaseContext(), "Please fill out the form", Toast.LENGTH_SHORT).show();
    }


}
