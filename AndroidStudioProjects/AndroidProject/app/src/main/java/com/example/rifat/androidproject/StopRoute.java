package com.example.rifat.androidproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class StopRoute extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_route);

        Bundle bundle = getIntent().getExtras();
        StopRouteFragment fragment = new StopRouteFragment();
        fragment.setIsTablet(false);
        fragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutStopRoute, fragment);
        fragmentTransaction.commit();
    }
}
