package com.ble.blescanner;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ScanDeviceFragment extends Fragment {

	//¸Ä¶¯1
	private final static String TAG = "GetBle";
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;

	private boolean mScanning;
	private Handler mHandler = new Handler();
	
	private Context context;

	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	
	
	
	
	public static OnNewDeviceAddedListener onNewDeviceAddedListener;

	public interface OnNewDeviceAddedListener {
		public void onNewDeviceAdded(String newDevice);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.scan_device_fragment, container, false);
				 
		final Button scan_btn = (Button)view.findViewById(R.id.scan_btn);
		
		scan_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				BleScan blescanservice = new BleScan(context);
				blescanservice.onSetUp();
				blescanservice.scanLeDevice(true);
				
			}
		});
				
		return view;
	}

	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			onNewDeviceAddedListener = (OnNewDeviceAddedListener) activity;
			context = activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement onNewDeviceAddedListener");
		}
	}
	 
}
