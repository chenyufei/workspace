package com.example.blemasterv2;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DeviceScanActivity extends ListActivity {

	private final static String TAG = "GetBle";
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();

	private boolean mScanning;
	private Handler mHandler;
	
	private Context context;

	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	
	
	
	public DeviceScanActivity(Context context){
		
		this.context = context;
		mHandler = new Handler();
	}
	
	public DeviceScanActivity(){
		
		//this.context = context;
		mHandler = new Handler();
	}
	

	

	public boolean onSetUp() {

		bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
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
			mHandler.postDelayed(new Runnable() {
			 	@Override
			 	public void run() {
					// mLeDeviceListAdapter.addDevice(device);
					// mLeDeviceListAdapter.notifyDataSetChanged();
//					String a = device.getName();
//					if(!(null==a))
//					MainActivity.devicename.setText(a);
					
//					String a = device.getName();
//					boolean repeat = false;
//					if(null !=  MainActivity.devices)
//					for(String b : MainActivity.devices)
//					{
//						if(b==a)
//							repeat = true;
//							//return; 
//					}
//					if(!repeat)
//						ScanDeviceFragment.onNewDeviceAddedListener.onNewDeviceAdded(a);
 			}
 			}, SCAN_PERIOD);
		}
	};
	
	 
	
	//adapter
	// Adapter for holding devices found through scanning.
		private class LeDeviceListAdapter extends BaseAdapter {
			private ArrayList<BluetoothDevice> mLeDevices;
			private LayoutInflater mInflator;

			public LeDeviceListAdapter() {
				super();
				mLeDevices = new ArrayList<BluetoothDevice>();
				mInflator = DeviceScanActivity.this.getLayoutInflater();
			}

			public void addDevice(BluetoothDevice device, int rssi) {
				if (!mLeDevices.contains(device)) {
					mLeDevices.add(device);
				}
				rssiMap.put(device, rssi);
			}

			public BluetoothDevice getDevice(int position) {
				return mLeDevices.get(position);
			}

			public void clear() {
				mLeDevices.clear();
			}

			@Override
			public int getCount() {
				return mLeDevices.size();
			}

			@Override
			public Object getItem(int i) {
				return mLeDevices.get(i);
			}

			@Override
			public long getItemId(int i) {
				return i;
			}

			@TargetApi(Build.VERSION_CODES.ECLAIR)
			@Override
			public View getView(int i, View view, ViewGroup viewGroup) {
				ViewHolder viewHolder;
				// General ListView optimization code.
				if (view == null) {
					view = mInflator.inflate(R.layout.listitem, null);
					viewHolder = new ViewHolder();
					viewHolder.deviceAddress = (TextView) view
							.findViewById(R.id.device_address);
					viewHolder.deviceName = (TextView) view
							.findViewById(R.id.device_name);
					viewHolder.deviceRssi = (TextView) view
							.findViewById(R.id.device_rssi);
					viewHolder.conn_btn = (Button)findViewById(R.id.conn_btn);

					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}

				BluetoothDevice device = mLeDevices.get(i);
				final String deviceName = device.getName();
				if (deviceName != null && deviceName.length() > 0)
					viewHolder.deviceName.setText(deviceName);
				else
				viewHolder.deviceName.setText("unknow Device");
				viewHolder.deviceAddress.setText(device.getAddress());
				viewHolder.deviceRssi.setText("" + rssiMap.get(device) + "db");

				return view;
			}
		}
 

		static class ViewHolder {
			TextView deviceName;
			TextView deviceAddress;
			TextView deviceRssi;
			Button conn_btn;
		}
	
	
	
 
}
