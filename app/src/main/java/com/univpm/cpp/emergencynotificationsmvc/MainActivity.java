package com.univpm.cpp.emergencynotificationsmvc;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.controllers.HomeFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.LoginFragment;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
