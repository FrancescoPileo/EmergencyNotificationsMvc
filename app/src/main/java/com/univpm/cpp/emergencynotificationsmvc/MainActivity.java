package com.univpm.cpp.emergencynotificationsmvc;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.univpm.cpp.emergencynotificationsmvc.controllers.HomeFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.LoginFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.RegistrationFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.MyBluetoothManager;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.utils.Directories;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_BT_PERMISSIONS = 0;
    public static final int REQUEST_LOCATION_PERMISSIONS = 1;
    public static final int REQUEST_STORAGE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        checkFolders();
        LocalSQLiteInitTask task = new LocalSQLiteInitTask();
        task.execute((Void) null);

        FirebaseMessaging.getInstance().subscribeToTopic("EN");
        FirebaseInstanceId.getInstance().getToken();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment(), LoginFragment.TAG);
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
        Fragment registrationFragment =  getSupportFragmentManager().findFragmentByTag(RegistrationFragment.TAG);
        if (registrationFragment != null && registrationFragment.isVisible()){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new LoginFragment(), LoginFragment.TAG);
            transaction.addToBackStack(null);
            transaction.commit();

        } else {
            finish();
        }

        //super.onBackPressed();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("DESTROOOOYYYYYYY", "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("PAUSEEEEEEEEEE", "");
    }

    @Override
    protected void onStop() {
        /*Intent intent = new Intent(this, CloseSessionService.class);
        startService(intent);*/
        super.onStop();
        Log.w("STOOOOOOOOOOOOOOOOOOP", "");
    }

    private void checkPermissions() {
        /*this.requestPermissions(
                new String[]{
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN
                },
                REQUEST_BT_PERMISSIONS);*/

        /*this.requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                },
                REQUEST_LOCATION_PERMISSIONS);*/

        this.requestPermissions(
                new String[]{
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                REQUEST_STORAGE_PERMISSIONS);


    }

    private void checkFolders() {
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            for (String dir: Directories.DIRS) {
                File directory = new File(dir);
                if (!directory.exists()){
                    directory.mkdir();
                }
            }

            File noMedia = new File (Directories.NOMEDIA);
            if (!noMedia.exists()){
                try {
                    noMedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }/* else {
            this.requestPermissions(
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_STORAGE_PERMISSIONS);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_STORAGE_PERMISSIONS:
                checkFolders();
                break;
            case REQUEST_BT_PERMISSIONS:
                break;
            case REQUEST_LOCATION_PERMISSIONS:
                break;
        }
    }

    public class CloseSessionService extends IntentService {


        public CloseSessionService(){
            super("CloseSessionService");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            Log.w("SessionService", "run");
            /*LocalPreferences localPreferences = new LocalPreferencesImpl(getApplicationContext());
            Session session = new Session();
            session.setUsername(localPreferences.getUsername());
            session.setTimeIn(localPreferences.getTimeIn());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = new Date();
            session.setTimeOut(dateFormat.format(time));

            SessionModel mSessionModel = new SessionModelImpl();
            mSessionModel.updateSession(session);*/
        }
    }

    public class LocalSQLiteInitTask extends AsyncTask<Void, Void, Boolean> {

        SQLiteDatabase db = null;
        LocalSQLiteDbHelper helper = null;

        @Override
        protected Boolean doInBackground(Void... voids) {
            helper = new LocalSQLiteDbHelper(getApplicationContext());
            db = helper.getReadableDatabase();
            return (db != null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            helper.close();
        }
    }


}
