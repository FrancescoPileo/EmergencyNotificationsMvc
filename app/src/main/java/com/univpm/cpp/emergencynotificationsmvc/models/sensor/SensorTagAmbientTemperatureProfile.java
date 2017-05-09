package com.univpm.cpp.emergencynotificationsmvc.models.sensor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;

import java.util.List;

public class SensorTagAmbientTemperatureProfile extends GenericBluetoothProfile {

	public SensorTagAmbientTemperatureProfile(Context con, BluetoothDevice device, BluetoothGattService service, BluetoothLeService controller) {
		super(con,device,service,controller);

		this.description = "Temperature";

		List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();
		
		for (BluetoothGattCharacteristic c : characteristics) {
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_IRT_DATA.toString())) {
				this.dataC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_IRT_CONF.toString())) {
				this.configC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_IRT_PERI.toString())) {
				this.periodC = c;
			}
		}

	}


	public static boolean isCorrectService(BluetoothGattService service) {
		if ((service.getUuid().toString().compareTo(SensorTagGatt.UUID_IRT_SERV.toString())) == 0) {
			return true;
		}
		else return false;
	}

	@Override
	public double[] getDoubleValues() {
		double[] values = new double[1];
		byte[] value = this.dataC.getValue();
		int offset = 2;
		Integer lowerByte = (int) value[offset] & 0xFF;
		Integer upperByte = (int) value[offset+1] & 0xFF;
		Integer shortInt =  (upperByte << 8) + lowerByte;
        values[0] = shortInt / 128.0;
		return values;
	}

	public void configureService() {
		int error = mBTLeService.writeCharacteristic(this.configC, (byte)0x01);
		if (error != 0) {
			if (this.configC != null)
				Log.d("AmbientTempProfile","Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + error);
		}
		error = this.mBTLeService.setCharacteristicNotification(this.dataC, true);
		if (error != 0) {
			if (this.dataC != null)
				Log.d("AmbientTempProfile","Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + error);
		}

		this.isConfigured = true;
	}
	public void deConfigureService() {
		int error = mBTLeService.writeCharacteristic(this.configC, (byte)0x00);
		if (error != 0) {
			if (this.configC != null)
				Log.d("AmbientTempProfile","Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + error);
		}
		error = this.mBTLeService.setCharacteristicNotification(this.dataC, false);
		if (error != 0) {
			if (this.dataC != null)
				Log.d("AmbientTempProfile","Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + error);
		}
		this.isConfigured = false;
	}


}
