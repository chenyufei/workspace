package com.example.blemasterv3;

import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */

public class BluetoothLeService extends Service {

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;

	private BLEServiceCallback mBLEServiceCb = null;
	
	private final IBinder mBinder = new LocalBinder();

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
			UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		if(null == mBluetoothManager){
			mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
			if(null == mBluetoothManager){
				mBLEServiceCb.showResult("unable to initialize BluetoothManager");
			return;			
			}
		}
		
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if(null == mBluetoothAdapter){
			mBLEServiceCb.showResult("Unable to obtain a BluetoothAdapter");
			return;
		}

	}

	public boolean connect(final String address) {
		if(null == mBluetoothAdapter || null == address){
			Log.w("BluetoothAdapter not initialized or unspecified address");
			return false;
		}
		
		if(mBluetoothDeviceAddress != null && address.equalsIgnoreCase(address)&& mBluetoothGatt != null){
			Log.d("trying to use an existing mBluetoothGatt for connection");
			if(mBluetoothGatt.connect()){
				mConnectionState = STATE_CONNECTING;
				return true;
			}else{
				return false;
			}
		}
		
		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if(null == device){
			Log.w("Device not found");
			return false;
		}
		
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		mBLEServiceCb.displayConnectionState(true);
		
		return true;
	}

	
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			super.onConnectionStateChange(gatt, status, newState);
			
	           if (newState == BluetoothProfile.STATE_CONNECTED) {
	                if (mBLEServiceCb != null) {
	                    mBLEServiceCb.notifyConnectedGATT();
	                }

	                Log.d("Connected to GATT server.");
	                // Attempts to discover services after successful connection.
	                Log.d("Attempting to start service discovery:" +
	                        mBluetoothGatt.discoverServices());
	      //          startReadRssi();

	            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
	                if (mBLEServiceCb != null) {
	                    mBLEServiceCb.notifyDisconnectedGATT();
	                }

	     //           stopReadRssi();
	                Log.d("Disconnected from GATT server.");
	                 
	            }
			
		}
		
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onServicesDiscovered(gatt, status);
			
			 Log.d("onServicesDiscovered status = " + status);

	            if (status == BluetoothGatt.GATT_SUCCESS) {
	                if (mBLEServiceCb != null) {
	                    mBLEServiceCb.displayGATTServices();
	                }

	            } else {
	                Log.d("onServicesDiscovered received: " + status);
	            }
			
			
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicRead(gatt, characteristic, status);
			
//			Log.d("onCharacteristcisRead status:" + status);
//			if(status == BluetoothGatt.GATT_SUCCESS){
//				  displayCharacteristic(characteristic);
//			}else{
//				Log.d("onServiceDiscovered received: " + status);
//			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
		}
		
		
	};
	 
	 private void displayCharacteristic(final BluetoothGattCharacteristic characteristic) {
		 String msg = null;
		 
	        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
	            int flag = characteristic.getProperties();
	            int format = -1;
	            if ((flag & 0x01) != 0) {
	                format = BluetoothGattCharacteristic.FORMAT_UINT16;
	                Log.d("Heart rate format UINT16.");
	            } else {
	                format = BluetoothGattCharacteristic.FORMAT_UINT8;
	                Log.d("Heart rate format UINT8.");
	            }
	            final int heartRate = characteristic.getIntValue(format, 1);
	            Log.d(String.format("Received heart rate: %d", heartRate));

	            msg = String.valueOf(heartRate);
	        } else {
	            // For all other profiles, writes the data formatted in HEX.
	            final byte[] data = characteristic.getValue();
	            if (data != null && data.length > 0) {
	                final StringBuilder stringBuilder = new StringBuilder(data.length);
	                for(byte byteChar : data)
	                    stringBuilder.append(String.format("%02X ", byteChar));

	                msg = new String(data) + "\n" + stringBuilder.toString();
	                
	            }
	        }
	        if (mBLEServiceCb != null) {
	            mBLEServiceCb.displayData(msg);
	        }
	        
	 }
	
	public class LocalBinder extends Binder {
		BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	

	
	public void setBLEServiceCb(BLEServiceCallback cb) {
        if (cb != null) {
            mBLEServiceCb = cb;
        }
    }
	
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        
        return mBluetoothGatt.getServices();
    }
    
	public interface BLEServiceCallback {
		public void displayRssi(int rssi);

		public void displayData(String data);

		public void notifyConnectedGATT();

		public void notifyDisconnectedGATT();

		public void displayGATTServices();

		public void showResult(String information);

		public void displayConnectionState(boolean connState);
	}
}
