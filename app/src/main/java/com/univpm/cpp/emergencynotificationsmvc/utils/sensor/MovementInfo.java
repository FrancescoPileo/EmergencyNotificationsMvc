package com.univpm.cpp.emergencynotificationsmvc.utils.sensor;

public class MovementInfo {

    private double acc_x;
    private double acc_y;
    private double acc_z;
    private double gyr_x;
    private double gyr_y;
    private double gyr_z;
    private double mag_x;
    private double mag_y;
    private double mag_z;

    public MovementInfo(){

    }

    public MovementInfo(double acc_x, double acc_y, double acc_z, double gyr_x, double gyr_y,
                        double gyr_z, double mag_x, double mag_y, double mag_z) {
        this.acc_x = acc_x;
        this.acc_y = acc_y;
        this.acc_z = acc_z;
        this.gyr_x = gyr_x;
        this.gyr_y = gyr_y;
        this.gyr_z = gyr_z;
        this.mag_x = mag_x;
        this.mag_y = mag_y;
        this.mag_z = mag_z;
    }

    public double getAcc_x() {
        return acc_x;
    }

    public void setAcc_x(double acc_x) {
        this.acc_x = acc_x;
    }

    public double getAcc_y() {
        return acc_y;
    }

    public void setAcc_y(double acc_y) {
        this.acc_y = acc_y;
    }

    public double getAcc_z() {
        return acc_z;
    }

    public void setAcc_z(double acc_z) {
        this.acc_z = acc_z;
    }

    public double getGyr_x() {
        return gyr_x;
    }

    public void setGyr_x(double gyr_x) {
        this.gyr_x = gyr_x;
    }

    public double getGyr_y() {
        return gyr_y;
    }

    public void setGyr_y(double gyr_y) {
        this.gyr_y = gyr_y;
    }

    public double getGyr_z() {
        return gyr_z;
    }

    public void setGyr_z(double gyr_z) {
        this.gyr_z = gyr_z;
    }

    public double getMag_x() {
        return mag_x;
    }

    public void setMag_x(double mag_x) {
        this.mag_x = mag_x;
    }

    public double getMag_y() {
        return mag_y;
    }

    public void setMag_y(double mag_y) {
        this.mag_y = mag_y;
    }

    public double getMag_z() {
        return mag_z;
    }

    public void setMag_z(double mag_z) {
        this.mag_z = mag_z;
    }

    public String toString(){
        String str = "acc_x: " + getAcc_x();
        str += " acc_y: " + getAcc_y();
        str += " acc_z: " + getAcc_z();
        str += "; gyr_x: " + getGyr_x();
        str += " gyr_y: " + getGyr_y();
        str += " gyr_z: " + getGyr_z();
        str += "; mag_x: " + getMag_x();
        str += " mag_y: " + getMag_y();
        str += " mag_z: " + getMag_z();
        str +=".";

        return str;

    }
}


