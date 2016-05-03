package com.example.blemasterv3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private static final String LIST_NAME = "NAME";  
    private static final String LIST_UUID = "UUID";  
    
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    
    private ExpandableListAdapter madapter;
    
    
    private String mDeviceName;
    private String mDeviceAddress;
	
    private TextView devicename_tx;
    private TextView deviceaddress_tx;
    private TextView mConnectionState;
    
    
    private ExpandableListView mGattServicesList;
    private boolean mConnected = false;
	//定义两个List用来控制Group和Child中的String
	
	private  ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
     private List<List<HashMap<String, String>>> gattCharacteristicData = new ArrayList<List<HashMap<String, String>>>();
     private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
             new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
     
     private BluetoothGatt mBluetoothGatt;
     BluetoothGattCharacteristic characteristic;
     boolean enabled;
     
     private BluetoothLeService mBluetoothLeService;
     
     
     
     // Code to manage Service lifecycle.
     private ServiceConnection mServiceConnection = new ServiceConnection() {

         @Override
         public void onServiceConnected(ComponentName componentName, IBinder service) {
             mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
             if (!mBluetoothLeService.initialize()) {
                 //Log.e("Unable to initialize Bluetooth");
                 finish();
             }
             // Automatically connects to the device upon successful start-up initialization.
             String a = mDeviceAddress;
             mBluetoothLeService.connect(a);
             Log.w("a", "chushihua");
             mBluetoothLeService.setBLEServiceCb(mDCServiceCb);
         }

         @Override
         public void onServiceDisconnected(ComponentName componentName) {
             mBluetoothLeService = null;
         }
     };;
     

     private DCServiceCb mDCServiceCb = new DCServiceCb();
     
     
     
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gatt_services_characteristics);
		
		 mGattServicesList = (ExpandableListView)findViewById(R.id.gatt_services_list);
		
		madapter= new SimpleExpandableListAdapter(this, gattServiceData, android.R.layout.simple_expandable_list_item_2, 
				new String[]{ LIST_NAME, LIST_UUID}, new int[]{ android.R.id.text1, android.R.id.text2}, 
				gattCharacteristicData, android.R.layout.simple_expandable_list_item_2, 
				new String[] {LIST_NAME, LIST_UUID}, new int[] {android.R.id.text1, android.R.id.text2});
		
		    mGattServicesList.setAdapter(madapter);

		    devicename_tx =(TextView)findViewById(R.id.device_name_tx2);
	        deviceaddress_tx =  (TextView)this.findViewById(R.id.device_address_tx2);
	        mConnectionState = (TextView) findViewById(R.id.connection_state);
	        
			final Intent intent = getIntent();
	        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME).toString();
	        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS).toString();
	        
	        devicename_tx.setText(mDeviceName);
	        deviceaddress_tx.setText(mDeviceAddress);
	         
//	        
//		for(int i=0; i< 5; i++){
//	    	
//			HashMap<String, String> curGroupMap = new HashMap<String, String>();
//			gattServiceData.add(curGroupMap);
//			curGroupMap.put(LIST_NAME, "group"+i);
//			
//			
//			List<HashMap<String,String>> children = new ArrayList<HashMap<String, String>>();
//			for(int j=0;j<3;j++){
//				HashMap<String, String> curchildMap = new HashMap<String, String>();
//				children.add(curchildMap);
//				curchildMap.put(LIST_UUID, "child"+i);
//			}			
//			gattCharacteristicData.add(children);
//	     }
		
	     // Demonstrates how to iterate through the supported GATT
	        // Services/Characteristics.
	        // In this sample, we populate the data structure that is bound to the
	        // ExpandableListView on the UI.
	        
	        

		   
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        
        String a = mDeviceAddress;
        
      //  BluetoothLeService a
        mBluetoothLeService.connect(a);
	      
	}
     
	   private void displayGattServices(List<BluetoothGattService> gattServices) {
           if (gattServices == null) return;
           String uuid = null;
           String unknownServiceString = getResources().
                   getString(R.string.unknown_service);
           String unknownCharaString = getResources().
                   getString(R.string.unknown_characteristic);
           ArrayList<HashMap<String, String>> gattServiceData =
                   new ArrayList<HashMap<String, String>>();
           ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
           mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

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
     
	   
	   
	    private void updateConnectionState(final int resourceId) {
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                mConnectionState.setText(resourceId);
	            }
	        });
	    }
	   
	   
	   
	    private void clearUI() {
	        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);

	    }
	   
	   
	   public class DCServiceCb implements BluetoothLeService.BLEServiceCallback {
 

 

	        @Override
	        public void notifyConnectedGATT() {
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mConnected = true;
	                    updateConnectionState(R.string.connected);
	                    invalidateOptionsMenu();
	                }
	            });

	        }

	        @Override
	        public void notifyDisconnectedGATT() {
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mConnected = false;
	                    updateConnectionState(R.string.disconnected);
	                    invalidateOptionsMenu();
	                    clearUI();
	                }
	            });
	        }

	        @Override
	        public void displayGATTServices() {
	          //  Log.d("displayGATTServices.", mDeviceAddress);
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    if (mBluetoothLeService != null) {
	                        DeviceControlActivity.this.displayGattServices(
	                                mBluetoothLeService.getSupportedGattServices());
	                    }
	                }
	            });
	        }

			@Override
			public void displayRssi(int rssi) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void displayData(String data) {
				// TODO Auto-generated method stub
				
			}
 
	    }
	   
	   

}

	