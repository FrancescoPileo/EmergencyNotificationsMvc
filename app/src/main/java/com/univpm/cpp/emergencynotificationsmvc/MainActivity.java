package com.univpm.cpp.emergencynotificationsmvc;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.univpm.cpp.emergencynotificationsmvc.controllers.HomeFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.LoginFragment;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_BT_PERMISSIONS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBtPermissions();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment());
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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Intent intent = new Intent(this, CloseSessionService.class);
        startService(intent);
        super.onStop();
    }

    public void checkBtPermissions() {
        this.requestPermissions(
                new String[]{
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                },
                REQUEST_BT_PERMISSIONS);
    }




    public class CloseSessionService extends IntentService {


        public CloseSessionService(){
            super("CloseSessionService");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            Log.w("SessionService", "run");
            LocalPreferences localPreferences = new LocalPreferencesImpl(getApplicationContext());
            Session session = new Session();
            session.setUsername(localPreferences.getUsername());
            session.setTimeIn(localPreferences.getTimeIn());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = new Date();
            session.setTimeOut(dateFormat.format(time));

            SessionModel mSessionModel = new SessionModelImpl();
            mSessionModel.updateSession(session);
        }
    }
}
