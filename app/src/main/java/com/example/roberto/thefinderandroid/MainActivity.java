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
import com.example.roberto.thefinderandroid.Backend.CreateAccountAPICall;
import com.example.roberto.thefinderandroid.Backend.LogInAPICall;
import com.example.roberto.thefinderandroid.ResponseData.UserResponse;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, UserResponse.UserResponseCommunicator {
    EditText Username, pass;
    Button logIn, addAcount;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("UserName", "");
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
        addAcount = (Button) findViewById(R.id.addAcount);
        logIn.setOnClickListener(this);
        addAcount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.logIn){
            String userName = Username.getText().toString();
            String password = pass.getText().toString();

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                LogInAPICall call = new LogInAPICall(this);
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
    public void getUserResponse(UserResponse r) {
        if(r.status.equals("OK")) {
            com.example.roberto.thefinderandroid.DataModel.User user = r.results;
            if(user == null) Toast.makeText(getBaseContext(), "Wrong user name or password", Toast.LENGTH_SHORT).show();
            else {
                sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("UserID", user.ID);
                editor.putString("AuthToken", user.authToken);
                editor.commit();
                Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
                startActivity(intent);
            }
        }
        else Toast.makeText(getBaseContext(), "Servers are down", Toast.LENGTH_SHORT).show();
    }
}
