package com.example.rifat.androidproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class StopsDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_details);

        Bundle bundle = getIntent().getExtras();

        StopDetailsFragment fragment = new StopDetailsFragment();
        fragment.setIsTablet(false);
        fragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutStopDetails, fragment);
        fragmentTransaction.commit();
    }

    public void showRouteTrips(Bundle bundle){
        Intent intent = new Intent(this, StopRoute.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
