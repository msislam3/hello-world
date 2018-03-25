package com.example.rifat.androidproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
}
