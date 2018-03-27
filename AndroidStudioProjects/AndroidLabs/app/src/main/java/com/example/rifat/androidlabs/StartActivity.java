package com.example.rifat.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    private Button button;
    private Button buttonChat;
    private Button buttonWeather;
    private Button buttonToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 50);
            }
        });

        buttonChat = findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });

        buttonWeather = findViewById(R.id.buttonWeather);
        buttonWeather.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, WeatherActivity.class);
                startActivityForResult(intent, 50);
            }
        });


        buttonToolbar = findViewById(R.id.buttonToolbar);
        buttonToolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, TestToolbar.class);
                startActivity(intent);
            }
        });

        Log.i(ACTIVITY_NAME, "In onCreate()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==50) Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        if(resultCode==Activity.RESULT_OK) {
            String messagePassed = data.getStringExtra("Response");

            CharSequence text = "ListItemsActivity passed: " + messagePassed;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(StartActivity.this, text, duration);
            toast.show();
        }
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
