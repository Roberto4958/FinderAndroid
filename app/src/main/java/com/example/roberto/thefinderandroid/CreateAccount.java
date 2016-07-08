package com.example.roberto.thefinderandroid;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    private Button signUp;
    private EditText user, pass, firstName, lastName;
    private SharedPreferences sharedpreferences;

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
        if(userName.length()>0 && password.length()>0 && FName.length()>0 && LName.length()>0) {

            sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("UserName", userName);
            editor.putString("Paassword", password);
            editor.putString("FirstName", FName);
            editor.putString("LastName", LName);
            editor.putString("AuthToken", "lejfqerlbvlqhjevrljhervlqjhweqer");
            editor.commit();
            Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
            startActivity(intent);
        }
        else Toast.makeText(getBaseContext(), "Please fill out the form", Toast.LENGTH_SHORT).show();
    }
}
