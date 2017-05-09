package com.univpm.cpp.emergencynotificationsmvc.models.sensor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.NonNull;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Sensor {

    // Data
    private BluetoothDevice mBtDevice;
    private int mRssi;
    private List<BluetoothGattService> mServiceList = null;
    private List <BluetoothGattCharacteristic> mCharList = new ArrayList<BluetoothGattCharacteristic>();

    //Management
    private BluetoothGatt mBtGatt = null;
    private List<GenericBluetoothProfile> mProfiles;


    public Sensor(BluetoothDevice device, int rssi) {
        mBtDevice = device;
        mRssi = rssi;

        mProfiles = new ArrayList<GenericBluetoothProfile>();
    }

    public BluetoothDevice getBluetoothDevice() {
        return mBtDevice;
    }

    public int getRssi() {
        return mRssi;
    }

    public void setRssi(int rssiValue) {
        mRssi = rssiValue;
    }

    public void discoverServices() {
        // Create GATT object
        mBtGatt = BluetoothLeService.getBtGatt();
        if (mBtGatt.discoverServices()) {
            if (mServiceList != null) {
                mServiceList.clear();
            }
        } else {
            Log.w("Discovering services", "fail");
        }
    }

    public List<BluetoothGattService> getServiceList() {
        return mServiceList;
    }

    public void setServiceList(List<BluetoothGattService> mServiceList) {
        this.mServiceList = mServiceList;
    }

    public List<BluetoothGattCharacteristic> getCharList() {
        return mCharList;
    }

    public void setCharList(List<BluetoothGattCharacteristic> charList) {
        this.mCharList = charList;
    }

    public List<GenericBluetoothProfile> getProfiles() {
        return mProfiles;
    }
}
