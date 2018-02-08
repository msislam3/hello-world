package com.example.rifat.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private static final String PREFERENCE_FILE_KEY = "PREFERENCE";
    private Button loginButton;
    private EditText loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.button2);
        loginText = (EditText) findViewById(R.id.editText);

        SharedPreferences prefs = getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String email = prefs.getString("DefaultEmail", "email@domain.com"); //reads email address
        loginText.setText(email);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = loginText.getText().toString();

                SharedPreferences prefs = getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("DefaultEmail", email); //writes email address
                edit.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }

        });

        Log.i(ACTIVITY_NAME, "In onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
