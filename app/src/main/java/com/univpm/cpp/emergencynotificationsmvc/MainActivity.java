package com.univpm.cpp.emergencynotificationsmvc;

import android.Manifest;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.univpm.cpp.emergencynotificationsmvc.controllers.LoginFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.RegistrationFragment;
import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.MyBluetoothManager;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModel;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.Directories;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Broadcaster {

    public static final int REQUEST_PERMISSIONS = 0;

    public static final int CONNECTION_ONLINE = 0; //Connessione internet online, server online
    public static final int CONNECTION_OFFLINE = 1; //Connessione internet offline, (server indifferente)
    public static final int CONNECTION_SERVER_OFFLINE = 2; //Connessione internet online, server offline

    //Models
    private MapModel mMapModel = null;
    private BeaconModel mBeaconModel = null;
    private EnviromentalValuesModel mEnviromentalValuesModel = null;
    private LocalPreferences mLocalPreferences = null;
    private NodeModel mNodeModel = null;
    private PositionModel mPositionModel = null;
    private SessionModel mSessionModel = null;
    private UserModel mUserModel = null;

    private Fragment loginFragment;

    private MyBluetoothManager mBluetoothManager = null;

    //Gestione connessione
    public static ConnectivityManager mConnectivityManager;
    //public boolean mConnectionEnabled = false;
    private int mConnectionStatus;
    IntentFilter mFilter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        checkFolders();

        loginFragment = new LoginFragment();

        mFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(HttpUtils.INTENT_CONNECTION_REFUSED);
        registerReceiver(mReceiver, mFilter);

        mBluetoothManager = new MyBluetoothManager(getApplicationContext(), this);

        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        testConnection();

        /*String s = getIntent().getStringExtra("prova");
        if (s!= null) Log.w("prova", s);*/
    }

    private void modelsInit(){
        mLocalPreferences = new LocalPreferencesImpl(getApplicationContext());
        if (mConnectionStatus == CONNECTION_ONLINE){
            mMapModel = new MapModelImpl(this);
            mUserModel = new UserModelImpl(this);
            mNodeModel = new NodeModelImpl(this);
            mBeaconModel = new BeaconModelImpl(this);
            mPositionModel = new PositionModelImpl(this);
            mSessionModel = new SessionModelImpl(this);
            mEnviromentalValuesModel = new EnviromentalValuesModelImpl(this);
        } else {
            mMapModel = new MapModelLocalImpl(getApplicationContext());
            mUserModel = new UserModelLocalImpl(getApplicationContext());
            mNodeModel = new NodeModelLocalImpl(getApplicationContext());
            mBeaconModel = new BeaconModelLocalImpl(getApplicationContext());
            mPositionModel = new PositionModelLocalImpl(getApplicationContext());
            mEnviromentalValuesModel = new EnviromentalValuesModelLocalImpl(getApplicationContext());
        }
    }

    public void testConnection(){
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting() ) {
            ServerTestTask task = new ServerTestTask();
            task.execute((Void) null);
        } else {
            Toast.makeText(getApplicationContext(), "Nessuna connessione", Toast.LENGTH_LONG).show();
            mConnectionStatus = CONNECTION_OFFLINE;

            modelsInit();
            loadLoginFragment();
        }
        //modelsInit();
    }

    public class ServerTestTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            return HttpUtils.serverOn();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getApplicationContext(), "Modalità online", Toast.LENGTH_LONG).show();
                mConnectionStatus = CONNECTION_ONLINE;
            } else {
                Toast.makeText(getApplicationContext(), "Server offline", Toast.LENGTH_LONG).show();
                mConnectionStatus = CONNECTION_SERVER_OFFLINE;
            }
            //models initializations
            modelsInit();

            if (mConnectionStatus == CONNECTION_ONLINE) {
                FirebaseMessaging.getInstance().subscribeToTopic("EN");
                FirebaseInstanceId.getInstance().getToken();
            }

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                checkLocalDb();
            }
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //todo oasjdnakjosdnoaskndkoasn
        /*String s = getIntent().getStringExtra("prova");
        if (s!= null) Log.w("prova2", s);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyBluetoothManager.REQ_ENABLE_BT){
            if (resultCode == RESULT_OK){
                if (mBluetoothManager != null)
                    mBluetoothManager.init();
            }
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        Fragment registrationFragment =  getSupportFragmentManager().findFragmentByTag(RegistrationFragment.TAG);
        if (registrationFragment != null && registrationFragment.isVisible()){
           loadLoginFragment();
        } else {
            finish();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        /*Intent intent = new Intent(this, CloseSessionService.class);
        startService(intent);*/
        super.onStop();

    }

    private void checkPermissions() {

        this.requestPermissions(
                new String[]{
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                REQUEST_PERMISSIONS);


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
        }else {
            this.requestPermissions(
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_PERMISSIONS);
        }
    }

    private void checkLocalDb(){
        LocalSQLiteInitTask task = new LocalSQLiteInitTask();
        task.execute((Void) null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSIONS:
                checkFolders();
                checkLocalDb();
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

            helper = LocalSQLiteDbHelper.getInstance(getApplicationContext());
            db = helper.getReadableDatabase();
            return (db != null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
           loadLoginFragment();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                testConnection();
            } else if (HttpUtils.INTENT_CONNECTION_REFUSED.equals(action)){
                Toast.makeText(getApplicationContext(), "Connessione al server persa", Toast.LENGTH_LONG).show();
                testConnection();
            }
        }
    };

    private void loadLoginFragment(){
        //Se non è stato mai istanziato
        if (loginFragment == null) {
            Log.e("Solo una volta", "!!!");
            loginFragment  = new LoginFragment();
        }

        //Se è stato instanziato e visualizzato
        LoginFragment displayedLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LoginFragment.TAG);
        if (displayedLoginFragment != null){
            displayedLoginFragment.showReconnect(mConnectionStatus == CONNECTION_SERVER_OFFLINE);
            loginFragment = displayedLoginFragment;
        }

        //Visualizza il fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment, LoginFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public MapModel getMapModel() {
        return mMapModel;
    }

    public BeaconModel getBeaconModel() {
        return mBeaconModel;
    }

    public EnviromentalValuesModel getEnviromentalValuesModel() {
        return mEnviromentalValuesModel;
    }

    public LocalPreferences getLocalPreferences() {
        return mLocalPreferences;
    }

    public NodeModel getNodeModel() {
        return mNodeModel;
    }

    public PositionModel getPositionModel() {
        return mPositionModel;
    }

    public SessionModel getSessionModel() {
        return mSessionModel;
    }

    public UserModel getUserModel() {
        return mUserModel;
    }

    public int getmConnectionStatus() {
        return mConnectionStatus;
    }

    public MyBluetoothManager getBluetoothManager() {
        return mBluetoothManager;
    }
}
