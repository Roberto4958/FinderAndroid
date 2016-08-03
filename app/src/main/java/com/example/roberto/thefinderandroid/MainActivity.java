package com.example.roberto.thefinderandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Username, pass;
    private Button logIn, addAccount;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("AuthToken", "");
        if(user.length()>0){
            Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Username = (EditText)findViewById(R.id.userName);
        pass = (EditText)findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.logIn);
        addAccount = (Button) findViewById(R.id.addAcount);
        logIn.setOnClickListener(this);
        addAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.logIn){
            String userName = Username.getText().toString();
            String password = pass.getText().toString();

            if(userName.length()<1 || password.length()<1){
                Toast.makeText(getBaseContext(), "Please fill in the form", Toast.LENGTH_SHORT).show();
                return;
            }

            if(userName.contains("/")|| password.contains("/") ){
                Toast.makeText(getBaseContext(), "Please dont use /", Toast.LENGTH_SHORT).show();
                return;
            }

            else if(userName.contains("&")|| password.contains("&")){
                Toast.makeText(getBaseContext(), "Please dont use &", Toast.LENGTH_SHORT).show();
                return;
            }

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
                APIcomm call = new APIcomm(this);
                call.logIn(userName, password);
                startActivity(intent);
            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.addAcount){
            Intent intent = new Intent(".CreateAccount");
            startActivity(intent);
        }
    }
}
