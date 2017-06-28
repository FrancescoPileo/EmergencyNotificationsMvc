package com.univpm.cpp.emergencynotificationsmvc;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModel;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelLocalImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmergencyNotificationsMvc extends Application {


    private static final int REQ_ENABLE_BT = 0;
    public boolean mBtAdapterEnabled = false;
    public boolean mBleSupported = true;
    private BluetoothLeService mBluetoothLeService = null;
    private IntentFilter mFilter;
    public BluetoothAdapter mBtAdapter = null;
    public static BluetoothManager mBluetoothManager;

    public static ConnectivityManager mConnectivityManager;
    public boolean mConnectionEnabled = false;

    //Models
    MapModel mMapModel = null;
    BeaconModel mBeaconModel = null;
    EnviromentalValuesModel mEnviromentalValuesModel = null;
    LocalPreferences mLocalPreferences = null;
    NodeModel mNodeModel = null;
    PositionModel mPositionModel = null;
    SessionModel mSessionModel = null;
    UserModel mUserModel = null;

    @Override
    public void onCreate() {

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_LONG)
                    .show();
            mBleSupported = false;
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to BluetoothAdapter through MyBluetoothManager.
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = mBluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBtAdapter == null) {
            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_LONG).show();
            mBleSupported = false;
            return;
        }

        mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableIntent);
        }

        startBluetoothLeService();


        //Check internet connection
        mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        mConnectionEnabled = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        //models initializations
        //modelsInit(mConnectionEnabled);


        super.onCreate();

    }

    private void modelsInit(boolean connectionEnabled){
        mLocalPreferences = new LocalPreferencesImpl(getApplicationContext());
        if (connectionEnabled){
            mMapModel = new MapModelImpl();
            mUserModel = new UserModelImpl();
            mNodeModel = new NodeModelImpl();
            mBeaconModel = new BeaconModelImpl();
            mPositionModel = new PositionModelImpl();
            mSessionModel = new SessionModelImpl();
            mEnviromentalValuesModel = new EnviromentalValuesModelImpl();
        } else {
            mMapModel = new MapModelLocalImpl(getApplicationContext());
            mUserModel = new UserModelLocalImpl(getApplicationContext());
            mNodeModel = new NodeModelLocalImpl(getApplicationContext());
            mBeaconModel = new BeaconModelLocalImpl(getApplicationContext());
            mPositionModel = new PositionModelLocalImpl(getApplicationContext());
            mEnviromentalValuesModel = new EnviromentalValuesModelLocalImpl(getApplicationContext());
        }
    }



    // Code to manage Service life cycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize()) {
                //Toast.makeText(context, "Unable to initialize BluetoothLeService", Toast.LENGTH_SHORT).show();
                //finish();
                return;
            }
            final int n = mBluetoothLeService.numConnectedDevices();
            if (n > 0) {
                /*
                runOnUiThread(new Runnable() {
                    public void run() {
                        mThis.setError("Multiple connections!");
                    }
                });
                */
            } else {
                //startScan();
                // Log.i(TAG, "BluetoothLeService connected");
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            // Log.i(TAG, "BluetoothLeService disconnected");
        }
    };

    private void startBluetoothLeService() {
        boolean f;

        Intent bindIntent = new Intent(this, BluetoothLeService.class);
        startService(bindIntent);
        f = bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        if (!f) {
            Toast.makeText(this, "Bind to BluetoothLeService failed", Toast.LENGTH_LONG)
                    .show();
            //finish();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // Bluetooth adapter state change
                switch (mBtAdapter.getState()) {
                    case BluetoothAdapter.STATE_ON:
                        //ConnIndex = NO_DEVICE;
                        startBluetoothLeService();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        //Toast.makeText(context, R.string.app_closing, Toast.LENGTH_LONG)
                        //        .show();
                        //finish();
                        break;
                    default:
                        // Log.w(TAG, "Action STATE CHANGED not processed ");
                        break;
                }
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    Toast.makeText(context, "Modalità online", Toast.LENGTH_LONG).show();
                    mConnectionEnabled = true;
                } else {
                    Toast.makeText(context, "Modalità offline", Toast.LENGTH_LONG).show();
                    mConnectionEnabled = false;
                }
                modelsInit(mConnectionEnabled);
            }
        }
    };

    public boolean isConnectionEnabled() {
        return mConnectionEnabled;
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
}
