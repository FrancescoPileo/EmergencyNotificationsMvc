package com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModel;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.utils.sensor.MovementInfo;
import com.univpm.cpp.emergencynotificationsmvc.utils.sensor.Sensor;
import com.univpm.cpp.emergencynotificationsmvc.utils.sensor.GenericBluetoothProfile;
import com.univpm.cpp.emergencynotificationsmvc.utils.sensor.SensorTagAmbientTemperatureProfile;
import com.univpm.cpp.emergencynotificationsmvc.utils.sensor.SensorTagHumidityProfile;
import com.univpm.cpp.emergencynotificationsmvc.utils.sensor.SensorTagMovementProfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BluetoothManager {

    Context context;
    Activity activity;

    // Requests to other activities
    private static final int REQ_ENABLE_BT = 0;

    // BLE service management
    private boolean mBtAdapterEnabled = false;
    private boolean mBleSupported = true;
    private boolean mScanning = false;

    public static final String INIT_ACTION= "com.univpm.cpp.emergencynotificationsmvc.INIT";
    private IntentFilter mFilter;
    private BluetoothAdapter mBtAdapter = null;
    private static android.bluetooth.BluetoothManager mBluetoothManager;
    private BluetoothLeService mBluetoothLeService = null;
    private int mConnIndex = NO_DEVICE;

    //Devices management
    private String[] mDeviceFilter = null; //posso settare un filtro al nome dei dispositivi da cercare
    private int mNumDevs = 0;

    private List<Sensor> mSensorList;
    private Sensor mSensor;


    // Housekeeping
    private static final int NO_DEVICE = -1;
    private boolean mInitialised = false;
    private boolean firstNotify = true;
    private boolean scanQueue = false;

    private EnviromentalValuesModel mEnvValuesModel;
    private StoreTask storeTask;


    public BluetoothManager(Context context, Activity activity){

        this.context = context;
        this.activity = activity;

        //BluetoothLeService ready broadcast
        mFilter = new IntentFilter(INIT_ACTION);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        activity.registerReceiver(mReceiver, mFilter);

        mEnvValuesModel = new EnviromentalValuesModelImpl();

        if (BluetoothLeService.getInstance() != null){
            init();
        }
    }

    private void init(){

        //Inizializzazione Bluetooth
        mSensorList = new ArrayList<Sensor>();
        Resources res = getContext().getResources();
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
                        Resources res = activity.getResources();
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
       //scanning();
    }

    public void scanning(){

        IntentFilter mFilter2 = new IntentFilter(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        mFilter2.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter2.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        mFilter2.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
        mFilter2.addAction(BluetoothLeService.ACTION_DATA_READ);
        mFilter2.addAction(BluetoothLeService.ACTION_DATA_WRITE);
        activity.registerReceiver(mGattUpdateReceiver, mFilter2);

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
                    if (mSensorList.size()>0){
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

                    if (mSensorList.size() > 0) {
                        Log.w("Best Device", mSensor.getBluetoothDevice().getName());
                        Log.w("addess", mSensor.getBluetoothDevice().getAddress());

                        //Se non siamo connessi ad alcun dispositivo
                        if (mConnIndex == NO_DEVICE) {
                            onConnect();
                        } else {
                            if (mConnIndex != NO_DEVICE) {
                                mBluetoothLeService.disconnect(mSensor.getBluetoothDevice().getAddress());
                            }
                        }
                    }
                }
            }, 5000);
        } else {
            scanQueue = true;
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

    private void addSensor(Sensor device) {
        mNumDevs++;
        mSensorList.add(device);
    }

    private Sensor findSensor(BluetoothDevice device) {
        for (int i = 0; i < mSensorList.size(); i++) {
            if (mSensorList.get(i).getBluetoothDevice().getAddress()
                    .equals(device.getAddress())) {
                return mSensorList.get(i);
            }
        }
        return null;
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

    public void disconnect(){
        activity.unregisterReceiver(mGattUpdateReceiver);
        storeValues();
        int connState = mBluetoothManager.getConnectionState(mSensor.getBluetoothDevice(),
                BluetoothGatt.GATT);
        if (connState == BluetoothGatt.STATE_CONNECTED){
            mBluetoothLeService.disconnect(mSensor.getBluetoothDevice().getAddress());

            //azzerare variabili
            //mSensor = null;
        }
    }

    private void storeValues(){
        mSensor.calcAverageHum();
        mSensor.calcAverageTemp();
        mSensor.calcAverageMov();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        EnviromentalValues values = new EnviromentalValues(0, mSensor.getBluetoothDevice().getAddress(), dateFormat.format(date) , mSensor.getAverageTemp(),
                mSensor.getAverageHum(), mSensor.getAverageMov().getAcc_x(), mSensor.getAverageMov().getAcc_y(), mSensor.getAverageMov().getAcc_z(),
                mSensor.getAverageMov().getGyr_x(), mSensor.getAverageMov().getGyr_y(), mSensor.getAverageMov().getGyr_z(),
                mSensor.getAverageMov().getMag_x(), mSensor.getAverageMov().getMag_y(), mSensor.getAverageMov().getMag_z());

        storeTask = new StoreTask(values);
        storeTask.execute((Void) null);
    }

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
                            Sensor sensor = new Sensor(device, rssi);
                            addSensor(sensor);
                            Log.w("Dispositivo", "aggiunto");
                        } else {
                            // Already in list, update RSSI info
                            Sensor deviceInfo = findSensor(device);
                            deviceInfo.setRssi(rssi);
                            //Log.w("Dispositivo", "aggiornato");
                        }
                    }
                }

            });
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE);

            if (INIT_ACTION.equals(action)) {
                //BluetoothLeService ok
                init();
                if (scanQueue){
                    Log.w("ScanQueue", "matteo si sbaglia");
                    scanning();
                    scanQueue = false;
                }
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
            }
        }
    };


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            final String action = intent.getAction();
            final int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE);

           if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                // GATT connect
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //se avviene la connessione richiedo i servizi
                    mSensor.discoverServices();

                } else
                    setError("Connect failed. Status: " + status);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //todo gestire
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

                    mSensor.setServiceList(mBluetoothLeService.getSupportedGattServices());
                    if (mSensor.getServiceList().size() > 0) {
                        for (int ii = 0; ii < mSensor.getServiceList().size(); ii++) {
                            BluetoothGattService s = mSensor.getServiceList().get(ii);
                            List<BluetoothGattCharacteristic> c = s.getCharacteristics();
                            if (c.size() > 0) {
                                for (int jj = 0; jj < c.size(); jj++) {
                                    mSensor.getCharList().add(c.get(jj));
                                }
                            }
                        }
                    }
                    Log.w("DeviceActivity","Total characteristics " + mSensor.getCharList().size());
                    Thread worker = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //Iterate through the services and add GenericBluetoothServices for each service
                            int servicesDiscovered = 0;
                            int totalCharacteristics = 0;
                            //serviceList = mBtLeService.getSupportedGattServices();
                            for (BluetoothGattService s : mSensor.getServiceList()) {
                                List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
                                totalCharacteristics += chars.size();
                            }

                            for (int ii = 0; ii < mSensor.getServiceList().size(); ii++) {
                                BluetoothGattService s = mSensor.getServiceList().get(ii);
                                List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
                                if (chars.size() == 0) {
                                    Log.d("DeviceActivity", "No characteristics found for this service !!!");
                                    return;
                                }
                                servicesDiscovered++;

                                if (SensorTagAmbientTemperatureProfile.isCorrectService(s)) {
                                    SensorTagAmbientTemperatureProfile amt = new SensorTagAmbientTemperatureProfile(getContext(), mSensor.getBluetoothDevice(), s, mBluetoothLeService);
                                    mSensor.getProfiles().add(amt);
                                } else if (SensorTagHumidityProfile.isCorrectService(s)){
                                    SensorTagHumidityProfile hmt = new SensorTagHumidityProfile(getContext(), mSensor.getBluetoothDevice(), s, mBluetoothLeService);
                                    mSensor.getProfiles().add(hmt);
                                } else if (SensorTagMovementProfile.isCorrectService(s)){
                                    SensorTagMovementProfile mvt = new SensorTagMovementProfile(getContext(), mSensor.getBluetoothDevice(), s, mBluetoothLeService);
                                    mSensor.getProfiles().add(mvt);
                                }
                            }

                            for (final GenericBluetoothProfile p : mSensor.getProfiles()) {
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

                if (firstNotify){
                    firstNotify = false;

                    Handler stopNotify = new Handler();
                    stopNotify.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            disconnect();
                            firstNotify = true;
                        }
                    }, 5000);

                }

                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                //Log.d("DeviceActivity","Got Characteristic : " + uuidStr);
                for (int ii = 0; ii < mSensor.getCharList().size(); ii++) {
                    BluetoothGattCharacteristic tempC = mSensor.getCharList().get(ii);
                    if ((tempC.getUuid().toString().equals(uuidStr))) {
                        for (int jj = 0; jj < mSensor.getProfiles().size(); jj++) {
                            GenericBluetoothProfile p = mSensor.getProfiles().get(jj);
                            if (p.isDataC(tempC)) {

                                if (p.getDescription().equals("Temperature")) {
                                    mSensor.getTemperatures().add(p.getDoubleValues()[0]);
                                } else if (p.getDescription().equals("Humidity")) {
                                    mSensor.getHumidities().add(p.getDoubleValues()[0]);
                                } else if (p.getDescription().equals("Movement")) {
                                    double[] values = p.getDoubleValues();
                                    MovementInfo mi = new MovementInfo(values[0], values[1], values[2],
                                            values[3], values[4], values[5],
                                            values[6], values[7], values[8]);
                                    mSensor.getMovements().add(mi);
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
                for (int ii = 0; ii < mSensor.getCharList().size(); ii++) {
                    BluetoothGattCharacteristic tempC = mSensor.getCharList().get(ii);
                    if ((tempC.getUuid().toString().equals(uuidStr))) {
                        for (int jj = 0; jj < mSensor.getProfiles().size(); jj++) {
                            GenericBluetoothProfile p = mSensor.getProfiles().get(jj);
                        }
                        //Log.d("DeviceActivity","Got Characteristic : " + tempC.getUuid().toString());
                        break;
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
                // Data read
                byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                for (int ii = 0; ii < mSensor.getCharList().size(); ii++) {
                    BluetoothGattCharacteristic tempC = mSensor.getCharList().get(ii);
                    if ((tempC.getUuid().toString().equals(uuidStr))) {
                        for (int jj = 0; jj < mSensor.getProfiles().size(); jj++) {
                            GenericBluetoothProfile p = mSensor.getProfiles().get(jj);
                        }
                        //Log.d("DeviceActivity","Got Characteristic : " + tempC.getUuid().toString());
                        break;
                    }
                }
            }
        }
    };

    void setError(String txt) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), txt, Toast.LENGTH_LONG).show();
            }
        });
    }

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public Sensor getSensor() {
        return mSensor;
    }


    // Dato il nome della mappa preso dallo Spinner, invoca setMapOnView passandogli il path preso dal db
    public class StoreTask extends AsyncTask<Void, Void, Boolean> {

        private EnviromentalValues values;

        StoreTask(EnviromentalValues values) {
            this.values = values;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            mEnvValuesModel.newValues(values);
            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {

            }
            else {
                Log.w("Store", "error");
            }
        }


    }
}