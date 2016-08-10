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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.API.APIcomm;
import com.example.roberto.thefinderandroid.API.UserResponse;
import com.example.roberto.thefinderandroid.DataModel.User;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, UserResponse.UserResponseCommunicator{
    private EditText Username, pass;
    private Button logIn, addAccount;
    private SharedPreferences sharedpreferences;
    private ProgressBar progressBar;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Username = (EditText)findViewById(R.id.userName);
        pass = (EditText)findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.logIn);
        addAccount = (Button) findViewById(R.id.addAcount);
        logIn.setOnClickListener(this);
        addAccount.setOnClickListener(this);
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

        return true;
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

            if(!(checkIfValid(userName) && checkIfValid(password)) ){
                return;
            }

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                progressBar.setVisibility(View.VISIBLE);
                APIcomm call = new APIcomm(this);
                call.logIn(userName, password);
            }
            else Toast.makeText(getBaseContext(), "Counld not connect to network", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.addAcount){
            Intent intent = new Intent(".CreateAccount");
            startActivity(intent);
        }
    }

    @Override
    public void getUserResponse(User r) {
        progressBar.setVisibility(View.INVISIBLE);
        if(r == null){
            Toast.makeText(getBaseContext(), "Wrong user name or password", Toast.LENGTH_SHORT).show();
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
