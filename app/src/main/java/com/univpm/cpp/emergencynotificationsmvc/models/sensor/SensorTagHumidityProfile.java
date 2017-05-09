package com.univpm.cpp.emergencynotificationsmvc.models.sensor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;

import java.util.List;

public class SensorTagHumidityProfile extends GenericBluetoothProfile {


	public SensorTagHumidityProfile(Context con, BluetoothDevice device, BluetoothGattService service, BluetoothLeService controller) {
			super(con,device,service,controller);

			this.description = "Humidity";

			List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();
			
			for (BluetoothGattCharacteristic c : characteristics) {
				if (c.getUuid().toString().equals(SensorTagGatt.UUID_HUM_DATA.toString())) {
					this.dataC = c;
				}
				if (c.getUuid().toString().equals(SensorTagGatt.UUID_HUM_CONF.toString())) {
					this.configC = c;
				}
				if (c.getUuid().toString().equals(SensorTagGatt.UUID_HUM_PERI.toString())) {
					this.periodC = c;
				}
			}
		}
		
		public static boolean isCorrectService(BluetoothGattService service) {
			if ((service.getUuid().toString().compareTo(SensorTagGatt.UUID_HUM_SERV.toString())) == 0) {//service.getUuid().toString().compareTo(SensorTagGatt.UUID_HUM_DATA.toString())) {
				Log.d("Test", "Match !");
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
		int a = (upperByte << 8) + lowerByte;
		a = a - (a % 4);
		values[0] = (-6f) + 125f * (a / 65535f);
		return values;
	}
}
