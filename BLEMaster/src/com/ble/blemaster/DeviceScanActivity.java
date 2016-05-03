package com.ble.blemaster;

import java.util.ArrayList;
import java.util.HashMap;
 

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;



public class DeviceScanActivity extends Fragment {

	public static BluetoothAdapter mBluetoothAdapter;
	public static LeDeviceListAdapter mLeDeviceListAdapter;
	
	private boolean mScanning;
	private Handler mHandler;
	public static Context context;
	
	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	
	private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();

//	public DeviceScanActivity(Context context) {
//		this.context = context;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHandler = new Handler();

		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Ensures Bluetooth is available on the device and it is enabled. If
		// not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} 
	}
	
	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
	        new BluetoothAdapter.LeScanCallback() {
	    @Override
	    public void onLeScan(final BluetoothDevice device, final int rssi,
	            byte[] scanRecord) {
	    	mHandler.postDelayed((new Runnable() {
		           @Override
		           public void run() {
		               mLeDeviceListAdapter.addDevice(device,rssi);
		               mLeDeviceListAdapter.notifyDataSetChanged();
		           }
		       }), SCAN_PERIOD);
	   }
	};
	
	
	public class LeDeviceListAdapter extends BaseAdapter {
		
		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflater;
		
		 
		public LeDeviceListAdapter() {
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ); 
		}
		  
		@Override
		public int getCount() {
			return mLeDevices.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mLeDevices.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			
			//inflater the view
			if(null == convertView){
				convertView = mInflater.inflate(R.layout.devicelist, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceName = (TextView)convertView.findViewById(R.id.device_name);
				viewHolder.deviceAddress = (TextView)convertView.findViewById(R.id.device_address);
				viewHolder.deviceRssi = (TextView)convertView.findViewById(R.id.device_rssi);
				viewHolder.scanbtn = (Button)convertView.findViewById(R.id.scan_btn);
				
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			//display the new device
			BluetoothDevice device = mLeDevices.get(position);
			final String deviceName = device.getName();
			if(deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName);
			else
				viewHolder.deviceName.setText("unknown device");
			
			viewHolder.deviceAddress.setText(device.getAddress());
			viewHolder.deviceRssi.setText(""+rssiMap.get(device)+"db");
			 
			return convertView;
		}
		
		public void addDevice(BluetoothDevice device, int rssi){
			if(!mLeDevices.contains(device))
				mLeDevices.add(device);
			rssiMap.put(device, rssi);
		}
		
		public BluetoothDevice getDevice(int position){
			return mLeDevices.get(position);
		}
		
		public void clear(){
			mLeDevices.clear();
		}
	}
	
	   static class ViewHolder {
	        TextView deviceName;
	        TextView deviceAddress;
	        TextView deviceRssi;
	        Button scanbtn;
	    }
	   
	   
	   
		@Override
		@Deprecated
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);

			try {
				context = activity;
			} catch (ClassCastException e) {
				throw new ClassCastException(activity.toString()
						+ "must implement onNewDeviceAddedListener");
			}
		}
	
}
