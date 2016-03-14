package com.BLE.blelinker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ScanFragment extends Fragment {
	
	private OnNewDeviceAddedListener onNewItemListener;
	
	public interface OnNewDeviceAddedListener {
		public void onNewDeviceAdded(BleDevice newdevice);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
			
		View view = inflater.inflate(R.layout.scan_fragment, container, false);

		
		final Button scan_btn = (Button)view.findViewById(R.id.scan_btn);
		
		scan_btn.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi") @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*				MainActivity.bleinstance.onSetUp();
				MainActivity.bleinstance.scanLeDevice(true);*/
		//		MainActivity.testa.add();
				
				Toast.makeText(getContext(), "fwhefhw", Toast.LENGTH_SHORT);
				
				
			}
		});
		
		return view;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try{
			onNewItemListener = (OnNewDeviceAddedListener)activity;
		}catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implement onnewdeviceListener");
		}
	}
	
	 
}
