package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;
import com.univpm.cpp.emergencynotificationsmvc.models.sensor.GenericBluetoothProfile;
import com.univpm.cpp.emergencynotificationsmvc.models.sensor.Sensor;
import com.univpm.cpp.emergencynotificationsmvc.models.sensor.SensorTagAmbientTemperatureProfile;
import com.univpm.cpp.emergencynotificationsmvc.models.sensor.SensorTagHumidityProfile;
import com.univpm.cpp.emergencynotificationsmvc.models.sensor.SensorTagMovementProfile;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeView;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeViewImpl;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment2 extends Fragment implements
        HomeView.MapSpnItemSelectedViewListener{

    private HomeView mHomeView;

    // Requests to other activities
    private static final int REQ_ENABLE_BT = 0;

    // Housekeeping
    private static final int NO_DEVICE = -1;
    private boolean mInitialised = false;

    // BLE management
    private boolean mBtAdapterEnabled = false;
    private boolean mBleSupported = true;
    private boolean mScanning = false;
    private int mNumDevs = 0;
    private int mConnIndex = NO_DEVICE;
    private List<Sensor> mSensorList;
    private static BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBtAdapter = null;
    private Sensor mSensor = null;
    private BluetoothLeService mBluetoothLeService = null;
    private IntentFilter mFilter;
    private String[] mDeviceFilter = null;

    private BluetoothGatt mBtGatt = null;
    private List<BluetoothGattService> mServiceList = null;

    public final String INIT_ACTION= "com.univpm.cpp.emergencynotificationsmvc.INIT";

    private boolean isBusy = false;

    private List<GenericBluetoothProfile> mProfiles;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mHomeView.setMapSlectedListener(this);
        mHomeView.setToolbar(this);

        mProfiles = new ArrayList<GenericBluetoothProfile>();

        //BluetoothLeService ready broadcast
        mFilter = new IntentFilter(INIT_ACTION);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        mFilter.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
        mFilter.addAction(BluetoothLeService.ACTION_DATA_READ);
        mFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE);
        getActivity().registerReceiver(mGattUpdateReceiver, mFilter);

        if (BluetoothLeService.getInstance() != null){
            init();
        }

        return mHomeView.getRootView();
    }



    private void init(){

        //Inizializzazione Bluetooth
        mSensorList = new ArrayList<Sensor>();
        Resources res = getResources();
        mDeviceFilter = res.getStringArray(R.array.device_filter);


        if (!mInitialised) {
            // Broadcast receiver
            mBluetoothLeService = BluetoothLeService.getInstance();
            mBluetoothManager = mBluetoothLeService.getBtManager();
            mBtAdapter = mBluetoothManager.getAdapter();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mBtAdapter == null) {
                        Toast.makeText(getContext(), res.getString(R.string.bt_not_supported), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });

            mBtAdapterEnabled = mBtAdapter.isEnabled();
            if (mBtAdapterEnabled) {
                // Start straight away
                //startBluetoothLeService();
            } else {
                // Request BT adapter to be turned on
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                getActivity().startActivityForResult(enableIntent, REQ_ENABLE_BT);
            }
            mInitialised = true;
        }
        scanning();

    }

    private void scanning(){
        if (mInitialised) {

            //Inizia la scansione dei dispositivi ble
            startScan();


            //Interrompe la scansione dopo 5000ms (5s)
            Handler stopScanProg = new Handler();
            stopScanProg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanLeDevice(false);

                    //Individua il dispositivo più vicino
                    int maxRssi = 0;
                    Log.w("Numero Dispositivi: ", String.valueOf(mSensorList.size()));
                    if (mSensorList.size()>1){
                        mSensor = mSensorList.get(0);
                        maxRssi = mSensorList.get(0).getRssi();
                    }
                    for (int i = 1; i < mSensorList.size(); i++){
                        Log.w("Dispositivo " + i + ": ", String.valueOf(mSensorList.get(i).getRssi()));
                        if (mSensorList.get(i).getRssi() >= maxRssi) {
                            mSensor = mSensorList.get(i);
                            maxRssi = mSensorList.get(i).getRssi();
                        }
                    }
                    Log.w("Best Device", mSensor.getBluetoothDevice().getName());

                    //Se non siamo connessi ad alcun dispositivo
                    if (mConnIndex == NO_DEVICE) {
                        onConnect();
                    } else {
                        if (mConnIndex != NO_DEVICE) {
                            mBluetoothLeService.disconnect(mSensor.getBluetoothDevice().getAddress());
                        }
                    }
                }
            }, 5000);
        }
    }

    private void discoverServices() {
        // Create GATT object
        mBtGatt = BluetoothLeService.getBtGatt();
        if (mBtGatt.discoverServices()) {
            if (mServiceList != null) {
                mServiceList.clear();
                //setBusy(true);
            }
        } else {
            setError("Discovering services fail");
        }
    }

    private void startScan() {
        // Start device discovery
        if (mBleSupported) {
            mNumDevs = 0;
            mSensorList.clear();
            scanLeDevice(true);
            if (!mScanning) {
                setError("Device discovery start failed");
            }
        } else {
            setError("BLE not supported on this device");
        }
    }

    private boolean scanLeDevice(boolean enable) {

        if (enable) {
            Log.w("Scan", "Start");
            mScanning = mBtAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.w("Scan", "Stop");
            mScanning = false;
            mBtAdapter.stopLeScan(mLeScanCallback);
        }
        return mScanning;
    }

    void setError(String txt) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), txt, Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean checkDeviceFilter(String deviceName) {
        if (deviceName == null)
            return false;

        int n = mDeviceFilter.length;
        if (n > 0) {
            boolean found = false;
            for (int i = 0; i < n && !found; i++) {
                found = deviceName.equals(mDeviceFilter[i]);
            }
            return found;
        } else
            // Allow all devices if the device filter is empty
            return true;
    }

    private boolean deviceInfoExists(String address) {
        for (int i = 0; i < mSensorList.size(); i++) {
            if (mSensorList.get(i).getBluetoothDevice().getAddress()
                    .equals(address)) {
                return true;
            }
        }
        return false;
    }

    private Sensor createDeviceInfo(BluetoothDevice device, int rssi) {
        Sensor deviceInfo = new Sensor(device, rssi);

        return deviceInfo;
    }

    private void addDevice(Sensor device) {
        mNumDevs++;
        mSensorList.add(device);
    }

    private Sensor findDeviceInfo(BluetoothDevice device) {
        for (int i = 0; i < mSensorList.size(); i++) {
            if (mSensorList.get(i).getBluetoothDevice().getAddress()
                    .equals(device.getAddress())) {
                return mSensorList.get(i);
            }
        }
        return null;
    }

    void onConnect() {
        if (mNumDevs > 0) {
            int connState = mBluetoothManager.getConnectionState(mSensor.getBluetoothDevice(),
                    BluetoothGatt.GATT);

            switch (connState) {
                case BluetoothGatt.STATE_CONNECTED:
                    mBluetoothLeService.disconnect(null);
                    break;
                case BluetoothGatt.STATE_DISCONNECTED:
                    boolean ok = mBluetoothLeService.connect(mSensor.getBluetoothDevice().getAddress());
                    if (!ok) {
                        setError("Connect failed");
                    }
                    break;
                default:
                    setError("Device busy (connecting/disconnecting)");
                    break;
            }
        }
    }

    // Device scan callback.
    // NB! Nexus 4 and Nexus 7 (2012) only provide one scan result per scan
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            //Log.w("device", device.getName());
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // Filter devices
                    if (checkDeviceFilter(device.getName())) {
                        if (!deviceInfoExists(device.getAddress())) {
                            // New device
                            Sensor deviceInfo = createDeviceInfo(device, rssi);
                            addDevice(deviceInfo);
                            Log.w("Dispositivo", "aggiunto");
                        } else {
                            // Already in list, update RSSI info
                            Sensor deviceInfo = findDeviceInfo(device);
                            deviceInfo.setRssi(rssi);
                            Log.w("Dispositivo", "aggiornato");
                        }
                    }
                }

            });
        }
    };


    @Override
    public void onMapSpnItemSelected(String map) {
        Log.w("Spinner", "map");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onStop() {
        super.onStop();
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        List <BluetoothGattService> serviceList;
        List <BluetoothGattCharacteristic> charList = new ArrayList<BluetoothGattCharacteristic>();

        @Override
        public void onReceive(final Context context, Intent intent) {
            final String action = intent.getAction();
            final int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE);


            if (INIT_ACTION.equals(action)) {
                init();
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // Bluetooth adapter state change
                switch (mBtAdapter.getState()) {
                    case BluetoothAdapter.STATE_ON:
                        mConnIndex = NO_DEVICE;
                        //startBluetoothLeService();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, R.string.bt_turned_off, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        // Log.w(TAG, "Action STATE CHANGED not processed ");
                        break;
                }
            } else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                // GATT connect
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //se avviene la connessione richiedo i servizi
                    discoverServices();

                } else
                    setError("Connect failed. Status: " + status);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                // GATT disconnect
                //stopDeviceActivity();
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //mScanView.setStatus(mBluetoothDevice.getName() + " disconnected", STATUS_DURATION);
                } else {
                    // setError("Disconnect Status: " + HCIDefines.hciErrorCodeStrings.get(status));
                }
                mConnIndex = NO_DEVICE;
                mBluetoothLeService.close();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {

                    serviceList = mBluetoothLeService.getSupportedGattServices();
                    if (serviceList.size() > 0) {
                        for (int ii = 0; ii < serviceList.size(); ii++) {
                            BluetoothGattService s = serviceList.get(ii);
                            List<BluetoothGattCharacteristic> c = s.getCharacteristics();
                            if (c.size() > 0) {
                                for (int jj = 0; jj < c.size(); jj++) {
                                    charList.add(c.get(jj));
                                }
                            }
                        }
                    }
                    Log.w("DeviceActivity","Total characteristics " + charList.size());
                    Thread worker = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //Iterate through the services and add GenericBluetoothServices for each service
                            int servicesDiscovered = 0;
                            int totalCharacteristics = 0;
                            //serviceList = mBtLeService.getSupportedGattServices();
                            for (BluetoothGattService s : serviceList) {
                                List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
                                totalCharacteristics += chars.size();
                            }

                            final int final_totalCharacteristics = totalCharacteristics;
                            for (int ii = 0; ii < serviceList.size(); ii++) {
                                BluetoothGattService s = serviceList.get(ii);
                                List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
                                if (chars.size() == 0) {
                                    Log.d("DeviceActivity", "No characteristics found for this service !!!");
                                    return;
                                }
                                servicesDiscovered++;

                                if (SensorTagAmbientTemperatureProfile.isCorrectService(s)) {
                                    SensorTagAmbientTemperatureProfile amt = new SensorTagAmbientTemperatureProfile(getContext(), mSensor.getBluetoothDevice(), s, mBluetoothLeService);
                                    mProfiles.add(amt);
                                } else if (SensorTagHumidityProfile.isCorrectService(s)){
                                    SensorTagHumidityProfile hmt = new SensorTagHumidityProfile(getContext(), mSensor.getBluetoothDevice(), s, mBluetoothLeService);
                                    mProfiles.add(hmt);
                                } else if (SensorTagMovementProfile.isCorrectService(s)){
                                    SensorTagMovementProfile mvt = new SensorTagMovementProfile(getContext(), mSensor.getBluetoothDevice(), s, mBluetoothLeService);
                                    mProfiles.add(mvt);
                                }
                            }
                            for (final GenericBluetoothProfile p : mProfiles) {
                                Log.w("Enabling service", "ok");
                                p.enableService();
                                p.configureService();
                            }
                        }
                    });
                    worker.start();
                } else {
                    Toast.makeText(getContext(), "Service discovery failed",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
                // Notification
                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                //Log.d("DeviceActivity","Got Characteristic : " + uuidStr);
                for (int ii = 0; ii < charList.size(); ii++) {
                    BluetoothGattCharacteristic tempC = charList.get(ii);
                    if ((tempC.getUuid().toString().equals(uuidStr))) {
                        for (int jj = 0; jj < mProfiles.size(); jj++) {
                            GenericBluetoothProfile p = mProfiles.get(jj);
                            if (p.isDataC(tempC)) {
                                double[] values = p.getDoubleValues();
                                if (values.length>1){
                                    Log.w(p.getDescription(), String.format("%.1f", values[0]) + ", " +
                                            String.format("%.1f", values[1]) + ", " +
                                            String.format("%.1f", values[2]) + "; " +
                                            String.format("%.1f", values[3]) + ", " +
                                            String.format("%.1f", values[4]) + ", " +
                                            String.format("%.1f", values[5]) + "; " +
                                            String.format("%.1f", values[6]) + ", " +
                                            String.format("%.1f", values[7]) + ", " +
                                            String.format("%.1f", values[8]));
                                } else {
                                    Log.w( p.getDescription(), String.format("%.1f", values[0]));
                                }
                            }
                        }
                        break;
                    }
                }

                //onCharacteristicChanged(uuidStr, value);
            } else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
                // Data written
                byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                for (int ii = 0; ii < charList.size(); ii++) {
                    BluetoothGattCharacteristic tempC = charList.get(ii);
                    if ((tempC.getUuid().toString().equals(uuidStr))) {
                        for (int jj = 0; jj < mProfiles.size(); jj++) {
                            GenericBluetoothProfile p = mProfiles.get(jj);
                        }
                        //Log.d("DeviceActivity","Got Characteristic : " + tempC.getUuid().toString());
                        break;
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
                // Data read
                byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                for (int ii = 0; ii < charList.size(); ii++) {
                    BluetoothGattCharacteristic tempC = charList.get(ii);
                    if ((tempC.getUuid().toString().equals(uuidStr))) {
                        for (int jj = 0; jj < mProfiles.size(); jj++) {
                            GenericBluetoothProfile p = mProfiles.get(jj);
                        }
                        //Log.d("DeviceActivity","Got Characteristic : " + tempC.getUuid().toString());
                        break;
                    }
                }
            }
        }
    };


    


}
