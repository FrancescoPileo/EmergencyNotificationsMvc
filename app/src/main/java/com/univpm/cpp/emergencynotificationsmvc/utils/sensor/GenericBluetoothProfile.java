package com.univpm.cpp.emergencynotificationsmvc.utils.sensor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;


public class GenericBluetoothProfile {

    protected String description = "Generic";

    protected BluetoothDevice mBTDevice;
	protected BluetoothGattService mBTService;
	protected BluetoothLeService mBTLeService;
	protected BluetoothGattCharacteristic dataC;
	protected BluetoothGattCharacteristic configC;
	protected BluetoothGattCharacteristic periodC;
	protected static final int GATT_TIMEOUT = 250; // milliseconds
	protected Context context;
	protected boolean isRegistered;
    public boolean isConfigured;
    public boolean isEnabled;

	public GenericBluetoothProfile(final Context con, BluetoothDevice device, BluetoothGattService service, BluetoothLeService controller) {
		super();
        this.description = "General";
		this.mBTDevice = device;
		this.mBTService = service;
		this.mBTLeService = controller;
		this.dataC = null;
		this.periodC = null;
		this.configC = null;
		this.context = con;
		this.isRegistered = false;
	}

    public String getDescription() {
        return description;
    }

    public static boolean isCorrectService(BluetoothGattService service) {
		//Always return false in parent class
		return false;
	}
    public boolean isDataC(BluetoothGattCharacteristic c) {
        if (this.dataC == null) return false;
        if (c.equals(this.dataC)) return true;
        else return false;
    }
	public void configureService() {
        int error = this.mBTLeService.setCharacteristicNotification(this.dataC, true);
        if (error != 0) {
            if (this.dataC != null)
                printError("Sensor notification enable failed: ",this.dataC,error);
        }
		this.isConfigured = true;
	}
	public void deConfigureService() {
        int error = this.mBTLeService.setCharacteristicNotification(this.dataC, false);
        if (error != 0) {
            if (this.dataC != null)
                printError("Sensor notification disable failed: ",this.dataC,error);
        }
        this.isConfigured = false;
	}

	public void enableService () {
        int error = mBTLeService.writeCharacteristic(this.configC, (byte)0x01);
        if (error != 0) {
            if (this.configC != null)
                printError("Sensor enable failed: ",this.configC,error);
        }
        //this.periodWasUpdated(1000);
        this.isEnabled = true;
	}
	public void disableService () {
        int error = mBTLeService.writeCharacteristic(this.configC, (byte)0x00);
        if (error != 0) {
            if (this.configC != null)
                printError("Sensor disable failed: ",this.configC,error);
        }
        this.isConfigured = false;
	}


    public void printError (String msg, BluetoothGattCharacteristic c, int error) {
        try {
            Log.d("GenericBluetoothProfile", msg + c.getUuid().toString() + " Error: " + error);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double[] getDoubleValues(){
		double[] values = new double[10];
        values[0] = 0;
        return values;
	}

    public void periodWasUpdated(int period) {
        if (period > 2450) period = 2450;
        if (period < 100) period = 100;
        byte p = (byte)((period / 10) + 10);
        Log.d("GenericBluetoothProfile","Period characteristic set to :" + period);
        /*
		if (this.mBTLeService.writeCharacteristic(this.periodC, p)) {
			mBTLeService.waitIdle(GATT_TIMEOUT);
		} else {
			Log.d("GenericBluetoothProfile","Sensor period failed: " + this.periodC.getUuid().toString());
		}
		*/
        int error = mBTLeService.writeCharacteristic(this.periodC, p);
        if (error != 0) {
            if (this.periodC != null)
                printError("Sensor period failed: ",this.periodC,error);
        }
    }


}
