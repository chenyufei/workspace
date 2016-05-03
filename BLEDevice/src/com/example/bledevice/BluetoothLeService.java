package com.example.bledevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


//A service that interacts with the BLE device via the Android BLE API
public class BluetoothLeService extends Service {

	 private final static String TAG = BluetoothLeService.class.getSimpleName();

	    private BluetoothManager mBluetoothManager;
	    private BluetoothAdapter mBluetoothAdapter;
	    private String mBluetoothDeviceAddress;
	    private BluetoothGatt mBluetoothGatt;
	    private int mConnectionState = STATE_DISCONNECTED;

	    private static final int STATE_DISCONNECTED = 0;
	    private static final int STATE_CONNECTING = 1;
	    private static final int STATE_CONNECTED = 2;

	    public final static String ACTION_GATT_CONNECTED =
	            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	    public final static String ACTION_GATT_DISCONNECTED =
	            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	    public final static String ACTION_GATT_SERVICES_DISCOVERED =
	            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	    public final static String ACTION_DATA_AVAILABLE =
	            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	    public final static String EXTRA_DATA =
	            "com.example.bluetooth.le.EXTRA_DATA";

	    public final static UUID UUID_HEART_RATE_MEASUREMENT =
	            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	    
	    
	    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
	            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	    
	     
	    BluetoothGattCharacteristic characteristic;
	    boolean enabled;
	    
	    private final String LIST_NAME = "NAME";
	    private final String LIST_UUID = "UUID";

	    // Various callback methods defined by the BLE API.
	    private final BluetoothGattCallback mGattCallback =
	            new BluetoothGattCallback() {
	        @Override
	        public void onConnectionStateChange(BluetoothGatt gatt, int status,
	                int newState) {
	            String intentAction;
	            if (newState == BluetoothProfile.STATE_CONNECTED) {
	                intentAction = ACTION_GATT_CONNECTED;
	                mConnectionState = STATE_CONNECTED;
	                broadcastUpdate(intentAction);
	                Log.i(TAG, "Connected to GATT server.");
	                Log.i(TAG, "Attempting to start service discovery:" +
	                        mBluetoothGatt.discoverServices());

	            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
	                intentAction = ACTION_GATT_DISCONNECTED;
	                mConnectionState = STATE_DISCONNECTED;
	                Log.i(TAG, "Disconnected from GATT server.");
	                broadcastUpdate(intentAction);
	            }
	        }

	        @Override
	        // New services discovered
	        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
	            if (status == BluetoothGatt.GATT_SUCCESS) {
	                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
	            } else {
	                Log.w(TAG, "onServicesDiscovered received: " + status);
	            }
	        }

	        @Override
	        // Result of a characteristic read operation
	        public void onCharacteristicRead(BluetoothGatt gatt,
	                BluetoothGattCharacteristic characteristic,
	                int status) {
	            if (status == BluetoothGatt.GATT_SUCCESS) {
	                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
	            }
	        } 
	    };
	
	    private void broadcastUpdate(final String action) {
	        final Intent intent = new Intent(action);
	        sendBroadcast(intent);
	    }

	    private void broadcastUpdate(final String action,
	                                 final BluetoothGattCharacteristic characteristic) {
	        final Intent intent = new Intent(action);

	        // This is special handling for the Heart Rate Measurement profile. Data
	        // parsing is carried out as per profile specifications.
	        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
	            int flag = characteristic.getProperties();
	            int format = -1;
	            if ((flag & 0x01) != 0) {
	                format = BluetoothGattCharacteristic.FORMAT_UINT16;
	                Log.d(TAG, "Heart rate format UINT16.");
	            } else {
	                format = BluetoothGattCharacteristic.FORMAT_UINT8;
	                Log.d(TAG, "Heart rate format UINT8.");
	            }
	            final int heartRate = characteristic.getIntValue(format, 1);
	            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
	            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
	        } else {
	            // For all other profiles, writes the data formatted in HEX.
	            final byte[] data = characteristic.getValue();
	            if (data != null && data.length > 0) {
	                final StringBuilder stringBuilder = new StringBuilder(data.length);
	                for(byte byteChar : data)
	                    stringBuilder.append(String.format("%02X ", byteChar));
	                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
	                        stringBuilder.toString());
	            }
	        }
	        sendBroadcast(intent);
	    }
	    
	 // Handles various events fired by the Service.
	 // ACTION_GATT_CONNECTED: connected to a GATT server.
	 // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	 // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	 // ACTION_DATA_AVAILABLE: received data from the device. This can be a
	 // result of read or notification operations.
	 private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
	     @Override
	     public void onReceive(Context context, Intent intent) {
	         final String action = intent.getAction();
	         if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
	             mConnected = true;
	             updateConnectionState(R.string.connected);
	             invalidateOptionsMenu();
	         } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
	             mConnected = false;
	             updateConnectionState(R.string.disconnected);
	             invalidateOptionsMenu();
	             clearUI();
	         } else if (BluetoothLeService.
	                 ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
	             // Show all the supported services and characteristics on the
	             // user interface.
	             displayGattServices(mBluetoothLeService.getSupportedGattServices());
	         } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
	             displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
	         }
	     }
	 };
	 
	 // Demonstrates how to iterate through the supported GATT
	    // Services/Characteristics.
	    // In this sample, we populate the data structure that is bound to the
	    // ExpandableListView on the UI.
	    private void displayGattServices(List<BluetoothGattService> gattServices) {
	        if (gattServices == null) return;
	        String uuid = null;
	        String unknownServiceString = getResources().
	                getString(R.string.unknown_service);
	        String unknownCharaString = getResources().
	                getString(R.string.unknown_characteristic);
	        ArrayList<HashMap<String, String>> gattServiceData =
	                new ArrayList<HashMap<String, String>>();
	        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
	                = new ArrayList<ArrayList<HashMap<String, String>>>();
	        mGattCharacteristics =
	                new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

	        // Loops through available GATT Services.
	        for (BluetoothGattService gattService : gattServices) {
	            HashMap<String, String> currentServiceData =
	                    new HashMap<String, String>();
	            uuid = gattService.getUuid().toString();
	            currentServiceData.put(
	                    LIST_NAME, SampleGattAttributes.
	                            lookup(uuid, unknownServiceString));
	            currentServiceData.put(LIST_UUID, uuid);
	            gattServiceData.add(currentServiceData);

	            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
	                    new ArrayList<HashMap<String, String>>();
	            List<BluetoothGattCharacteristic> gattCharacteristics =
	                    gattService.getCharacteristics();
	            ArrayList<BluetoothGattCharacteristic> charas =
	                    new ArrayList<BluetoothGattCharacteristic>();
	           // Loops through available Characteristics.
	            for (BluetoothGattCharacteristic gattCharacteristic :
	                    gattCharacteristics) {
	                charas.add(gattCharacteristic);
	                HashMap<String, String> currentCharaData =
	                        new HashMap<String, String>();
	                uuid = gattCharacteristic.getUuid().toString();
	                currentCharaData.put(
	                        LIST_NAME, SampleGattAttributes.lookup(uuid,
	                                unknownCharaString));
	                currentCharaData.put(LIST_UUID, uuid);
	                gattCharacteristicGroupData.add(currentCharaData);
	            }
	            mGattCharacteristics.add(charas);
	            gattCharacteristicData.add(gattCharacteristicGroupData);
	         }
	    }
	    
	    
	    
	    mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

	    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
	            UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
	    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
	    mBluetoothGatt.writeDescriptor(descriptor);
	    
	    @Override
	 // Characteristic notification
	 public void onCharacteristicChanged(BluetoothGatt gatt,
	         BluetoothGattCharacteristic characteristic) {
	     broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
	 }
	    
	    
	    public void close() {
	        if (mBluetoothGatt == null) {
	            return;
	        }
	        mBluetoothGatt.close();
	        mBluetoothGatt = null;
	    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
