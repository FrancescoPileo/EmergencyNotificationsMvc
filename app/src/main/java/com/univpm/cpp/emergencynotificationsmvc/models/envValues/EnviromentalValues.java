package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;

import org.json.JSONException;
import org.json.JSONObject;

public class EnviromentalValues implements Jsonable{

    int idEnv;
    Beacon beacon;
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
        this.beacon = null;
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

    public EnviromentalValues(int idEnv, Beacon beacon, String time, double temperature, double humidity,
                              double accX, double accY, double accZ,
                              double gyrX, double gyrY, double gyrZ,
                              double magX, double magY, double magZ) {
        this.idEnv = idEnv;
        this.beacon = beacon;
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

    public EnviromentalValues(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.idEnv = jsonObject.getInt("idenv");
            this.beacon =  new Beacon(jsonObject.getJSONObject("idbeacon").toString());
            this.time = jsonObject.getString("detectiontime");
            this.temperature = jsonObject.getDouble("temperature");
            this.humidity = jsonObject.getDouble("humidity");
            this.accX = jsonObject.getDouble("accx");
            this.accY = jsonObject.getDouble("accy");
            this.accZ = jsonObject.getDouble("accz");
            this.gyrX = jsonObject.getDouble("gyrx");
            this.gyrY = jsonObject.getDouble("gyry");
            this.gyrZ = jsonObject.getDouble("gyrz");
            this.magX = jsonObject.getDouble("magx");
            this.magY = jsonObject.getDouble("magy");
            this.magZ = jsonObject.getDouble("magz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIdEnv() {
        return idEnv;
    }

    public void setIdEnv(int idEnv) {
        this.idEnv = idEnv;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
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

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("idenv", this.getIdEnv());
            jsonObject.put("idbeacon", this.getBeacon().toJson());
            jsonObject.put("detectiontime", this.getTime());
            jsonObject.put("temperature", this.getTemperature());
            jsonObject.put("humidity", this.getHumidity());
            jsonObject.put("accx", this.getAccX());
            jsonObject.put("accy", this.getAccY());
            jsonObject.put("accz", this.getAccZ());
            jsonObject.put("gyrx", this.getGyrX());
            jsonObject.put("gyry", this.getGyrY());
            jsonObject.put("gyrz", this.getGyrZ());
            jsonObject.put("magx", this.getMagX());
            jsonObject.put("magy", this.getMagY());
            jsonObject.put("magz", this.getMagZ());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
