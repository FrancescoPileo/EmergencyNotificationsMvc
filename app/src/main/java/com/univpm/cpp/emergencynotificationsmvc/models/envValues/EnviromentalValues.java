package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import java.util.Date;

public class EnviromentalValues {

    int idEnv;
    String idBeacon;
    String time;
    double temperature;
    double humidity;
    double accX;
    double accY;
    double accZ;
    double gyrX;
    double gyrY;
    double gyrZ;
    double magX;
    double magY;
    double magZ;

    public EnviromentalValues() {

        super();
        this.idEnv = -1;
        this.idBeacon = null;
        this.time = null;
        this.temperature = 0;
        this.humidity = 0;
        this.accX = 0;
        this.accY = 0;
        this.accZ = 0;
        this.gyrX = 0;
        this.gyrY = 0;
        this.gyrZ = 0;
        this.magX = 0;
        this.magY = 0;
        this.magZ = 0;

    }

    public EnviromentalValues(int idEnv, String idBeacon, String time, double temperature, double humidity,
                              double accX, double accY, double accZ,
                              double gyrX, double gyrY, double gyrZ,
                              double magX, double magY, double magZ) {
        this.idEnv = idEnv;
        this.idBeacon = idBeacon;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyrX = gyrX;
        this.gyrY = gyrY;
        this.gyrZ = gyrZ;
        this.magX = magX;
        this.magY = magY;
        this.magZ = magZ;
    }

    public int getIdEnv() {
        return idEnv;
    }

    public void setIdEnv(int idEnv) {
        this.idEnv = idEnv;
    }

    public String getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(String idBeacon) {
        this.idBeacon = idBeacon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getGyrX() {
        return gyrX;
    }

    public void setGyrX(double gyrX) {
        this.gyrX = gyrX;
    }

    public double getGyrY() {
        return gyrY;
    }

    public void setGyrY(double gyrY) {
        this.gyrY = gyrY;
    }

    public double getGyrZ() {
        return gyrZ;
    }

    public void setGyrZ(double gyrZ) {
        this.gyrZ = gyrZ;
    }

    public double getMagX() {
        return magX;
    }

    public void setMagX(double magX) {
        this.magX = magX;
    }

    public double getMagY() {
        return magY;
    }

    public void setMagY(double magY) {
        this.magY = magY;
    }

    public double getMagZ() {
        return magZ;
    }

    public void setMagZ(double magZ) {
        this.magZ = magZ;
    }
}
