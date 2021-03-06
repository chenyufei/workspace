package com.BLE.blelinker;


  

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.BLE.blelinker.ScanFragment.OnNewDeviceAddedListener;

public class BluetoothService extends Activity{

	//
	private OnNewDeviceAddedListener onNewDeviceAddedListener;
	
	private final static String TAG = "GetBle";
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;

	private boolean mScanning;
	private Handler mHandler;
	
	private Context context;

	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	
	public BluetoothService(Context context){
		
		this.context = context;
		mHandler = new Handler();
	}
	

	public boolean onSetUp() {

		bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		if (null == bluetoothManager) {
			Log.e(TAG, "null==bluetoothManager");
		}

		if (mBluetoothAdapter == null) {
			Log.e(TAG, "null == mBluetoothAdapter");
			return false;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Log.i(TAG, "enable mBluetoothAdapter");
			mBluetoothAdapter.enable();
		}

		return true;
	}

	public void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
	 			mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// mLeDeviceListAdapter.addDevice(device);
					// mLeDeviceListAdapter.notifyDataSetChanged();
					String a = device.getName();
					BleDevice newdevice = new BleDevice("null", "null", "null");
					boolean repeat = false;
					if(!(null == MainActivity.bledevices))
					for(BleDevice aa : MainActivity.bledevices){
						if(aa.getDevice_name().equals(a))
							repeat = true;
					}
					if(!repeat)
						newdevice.setDevice_name(a);
					
					if(!(null==newdevice)){
						newdevice.setDevice_name(a);
						onNewDeviceAddedListener.onNewDeviceAdded(newdevice);
					}
			//		MainActivity.devicename.setText(a);
				}
			});
		}
	};

}
