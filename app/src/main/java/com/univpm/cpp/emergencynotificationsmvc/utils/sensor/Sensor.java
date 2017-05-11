package com.univpm.cpp.emergencynotificationsmvc.utils.sensor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

public class Sensor {

    // Data
    private BluetoothDevice mBtDevice;
    private int mRssi;
    private List<BluetoothGattService> mServiceList = null;
    private List <BluetoothGattCharacteristic> mCharList = new ArrayList<BluetoothGattCharacteristic>();
    private ArrayList<Double> mTemperatures;
    private ArrayList<Double> mHumidities;
    private ArrayList<MovementInfo> mMovements;
    private double averageTemp;
    private double averageHum;
    private MovementInfo averageMov;

    //Management
    private BluetoothGatt mBtGatt = null;
    private List<GenericBluetoothProfile> mProfiles;


    public Sensor(BluetoothDevice device, int rssi) {
        mBtDevice = device;
        mRssi = rssi;

        mProfiles = new ArrayList<GenericBluetoothProfile>();

        mTemperatures = new ArrayList<>();
        mHumidities = new ArrayList<>();
        mMovements = new ArrayList<>();
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

    public ArrayList<Double> getTemperatures() {
        return mTemperatures;
    }

    public ArrayList<Double> getHumidities() {
        return mHumidities;
    }

    public ArrayList<MovementInfo> getMovements() {
        return mMovements;
    }

    public double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    public double getAverageHum() {
        return averageHum;
    }

    public void setAverageHum(double averageHum) {
        this.averageHum = averageHum;
    }

    public MovementInfo getAverageMov() {
        return averageMov;
    }

    public void setAverageMov(MovementInfo averageMov) {
        this.averageMov = averageMov;
    }

    public void calcAverageTemp(){
        int i = 0;
        double sum = 0;
        for (i = 0; i < mTemperatures.size(); i++){
            sum += mTemperatures.get(i);
        }
        averageTemp = sum / mTemperatures.size();
    }

    public void calcAverageHum(){
        int i = 0;
        double sum = 0;
        for (i = 0; i < mHumidities.size(); i++){
            sum += mHumidities.get(i);
        }
        averageHum = sum / mHumidities.size();
    }

    public void calcAverageMov(){
        int i = 0;
        double[] sum = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        int size = mMovements.size();
        for (i = 0; i < size; i++){
            sum[0] += mMovements.get(i).getAcc_x();
            sum[1] += mMovements.get(i).getAcc_y();
            sum[2] += mMovements.get(i).getAcc_z();
            sum[3] += mMovements.get(i).getGyr_x();
            sum[4] += mMovements.get(i).getGyr_y();
            sum[5] += mMovements.get(i).getGyr_z();
            sum[6] += mMovements.get(i).getMag_x();
            sum[7] += mMovements.get(i).getMag_y();
            sum[8] += mMovements.get(i).getMag_z();
        }

        averageMov = new MovementInfo(sum[0]/size, sum[1]/size, sum[2]/size,
                sum[3]/size, sum[4]/size, sum[5]/size,
                sum[6]/size, sum[7]/size, sum[8]/size);
    }
}
