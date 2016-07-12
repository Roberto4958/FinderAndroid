package com.example.roberto.thefinderandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roberto.thefinderandroid.CustomAdapter.StringSetAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText Username, pass;
    Button logIn, addAcount;
    private SharedPreferences sharedpreferences;

    String l1 = "1 37.402288 -122.096560 Pool";
    String l2 = "2 37.390421 -122.083752 Library";
    String l3 = "3 37.333287 -121.879723 SJSU";
    String l4 = "4 37.399986 -122.067555 Park";
    Set<String> Locations;

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
            if(userName.equals("user")&& password.equals("pass") ){

                sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("UserName", userName);
                editor.putString("Paassword", password);
                editor.putString("FirstName", "First Name");
                editor.putString("LastName", "Last Name");
                editor.putString("AuthToken", "lejfqerlbvlqhjevrljhervlqjhweqer");
               // editor.commit();

                Locations = new HashSet<String>();
                Locations.add(l1);
                Locations.add(l2);
                Locations.add(l3);
                Locations.add(l4);

                editor.putStringSet("Locations", Locations);
                editor.putInt("size", Locations.size()+1);
                editor.commit();

                Intent intent = new Intent("com.example.roberto.thefinderandroid.User");
                startActivity(intent);
            }
            else{
                Toast.makeText(getBaseContext(), "Wrong user name or password", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.addAcount){
            Intent intent = new Intent(".CreateAccount");
            startActivity(intent);
        }

    }
}
