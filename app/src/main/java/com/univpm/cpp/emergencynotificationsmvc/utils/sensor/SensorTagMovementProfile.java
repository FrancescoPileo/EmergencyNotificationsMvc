package com.univpm.cpp.emergencynotificationsmvc.utils.sensor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothLeService;

import java.util.List;
;

public class SensorTagMovementProfile extends GenericBluetoothProfile {

	public SensorTagMovementProfile(Context con, BluetoothDevice device, BluetoothGattService service, BluetoothLeService controller) {
		super(con,device,service,controller);

        this.description = "Movement";

        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();
		
		for (BluetoothGattCharacteristic c : characteristics) {
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_MOV_DATA.toString())) {
				this.dataC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_MOV_CONF.toString())) {
				this.configC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_MOV_PERI.toString())) {
				this.periodC = c;
			}
		}
		

	}
	
	public static boolean isCorrectService(BluetoothGattService service) {
		if ((service.getUuid().toString().compareTo(SensorTagGatt.UUID_MOV_SERV.toString())) == 0) {
			return true;
		}
		else return false;
	}
	@Override
	public void enableService() {
        byte b[] = new byte[] {0x7F,0x00};
        int error = mBTLeService.writeCharacteristic(this.configC, b);
        if (error != 0) {
            if (this.configC != null)
            Log.d("MovementProfile","Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + error);
        }
        error = this.mBTLeService.setCharacteristicNotification(this.dataC, true);
        if (error != 0) {
            if (this.dataC != null)
            Log.d("MovementProfile","Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + error);
        }
        periodWasUpdated(250);
        this.isEnabled = true;
	}

	@Override
	public void disableService() {
        int error = mBTLeService.writeCharacteristic(this.configC, new byte[] {0x00,0x00});
        if (error != 0) {
            if (this.configC != null)
            Log.d("MovementProfile","Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + error);
        }
        error = this.mBTLeService.setCharacteristicNotification(this.dataC, false);
        if (error != 0) {
            if (this.dataC != null)
            Log.d("MovementProfile","Sensor notification disable failed: " + this.configC.getUuid().toString() + " Error: " + error);
        }
        this.isEnabled = false;
	}

    @Override
    public double[] getDoubleValues() {
        double[] values = new double[10];
        byte[] value = this.dataC.getValue();
        //accelerazione x, y, z
        final float SCALE_A = (float) 4096.0;
        values[0] = ((((value[7]<<8) + value[6]) / SCALE_A) * -1);
        values[1] = ((value[9]<<8) + value[8]) / SCALE_A;
        values[2] = ((((value[11]<<8) + value[10]) / SCALE_A) * -1);
        //gyro x, y, z
        final float SCALE_G = (float) 128.0;
        values[3] = ((value[1]<<8) + value[0]) / SCALE_G;
        values[4] = ((value[3]<<8) + value[2]) / SCALE_G;
        values[5] = ((value[5]<<8) + value[4]) / SCALE_G;
        //magn x, y, z
        final float SCALE_M = (float) (32768 / 4912);
        values[6] = ((value[13]<<8) + value[12]) / SCALE_M;
        values[7] = ((value[15]<<8) + value[14]) / SCALE_M;
        values[8] = ((value[17]<<8) + value[16]) / SCALE_M;
        return values;

    }
}
