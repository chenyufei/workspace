package com.example.ble_linker;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BluetoothLE_Service{
	

	private final static String TAG = "GetBle";
	
	private boolean mScanning;
	private Handler mHandler;
	
	//Stops scanning after 10 seconds
	private final static int SCAN_PERIOD = 10000;
	
	private BluetoothAdapter  BluetoothAdapter;
	private BluetoothManager bluetoothManager;
	
	private List<BleDevice> mListBleDevices;
	
	private Context context;
	private Activity activity;
	
	public BluetoothLE_Service(Context context, Activity activity){
		this.context = context;
		this.activity = activity;
	}
	
	public boolean onSetupBle(){

		 bluetoothManager =
				(BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
		if(null == bluetoothManager)
			return false;
		
	BluetoothAdapter = bluetoothManager.getAdapter();
	if(null == BluetoothAdapter)
		return false;
	
	if (! BluetoothAdapter.isEnabled()) {
	  //  Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	     BluetoothAdapter.enable();
	}
	
	return true;
 
	// Ensures Bluetooth is available on the device and it is enabled. If not,
	// displays a dialog requesting user permission to enable Bluetooth.
	}
	
	public void scanBleDevice(final boolean enable)
	{
		if(enable)
		{
			//stops scanning after a pre-defined scan period
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.i(TAG, "stopLeScan");
					mScanning = false;
					BluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			
			Log.i(TAG, "startLeScan");
			mScanning = true;
			BluetoothAdapter.startLeScan(mLeScanCallback);
		}
		else
		{
			Log.i(TAG, "stopLeScan");
			mScanning = false;
			BluetoothAdapter.stopLeScan(mLeScanCallback);
		}		
	}
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
			new BluetoothAdapter.LeScanCallback() {
				
				@Override
				public void onLeScan(final BluetoothDevice arg0, final int arg1, final byte[] arg2) {
					// TODO Auto-generated method stub
					activity.runOnUiThread(new Runnable() {
				 		@Override
						public void run() {
							// TODO Auto-generated method stub
				 			//此处添加当有新的device的代码
				 			
				 			mListBleDevices.add(new BleDevice(arg0, arg1, arg2));
				 			MainActivity.adapter.notifyDataSetChanged();
				 			
							//Log.i(TAG, "callback "+ arg0.getName() + " rssi = " + arg1);
							//mListBleDevices.add(new BleDevice(arg0, arg1, arg2));
						}
					});
				}
			};


}
