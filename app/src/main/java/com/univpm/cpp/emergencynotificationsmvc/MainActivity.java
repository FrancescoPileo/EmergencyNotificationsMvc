package com.univpm.cpp.emergencynotificationsmvc;

import android.Manifest;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.univpm.cpp.emergencynotificationsmvc.controllers.HomeFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.LoginFragment;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_BT_PERMISSIONS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBtPermissions();

        LocalPreferencesImpl localPreferences = new LocalPreferencesImpl(getApplicationContext());
        Fragment newFragment = null;
        Log.w("Loged", String.valueOf(localPreferences.alreadyLoged()));

        if (localPreferences.alreadyLoged()){
            //carica il fragment della home
            newFragment = new HomeFragment();
        } else {
            //carica il fragment di login
            newFragment = new LoginFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        //todo sistemare on back pressed
        finish();
        //super.onBackPressed();
    }

    public void checkBtPermissions() {
        this.requestPermissions(
                new String[]{
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                },
                REQUEST_BT_PERMISSIONS);
    }
}
