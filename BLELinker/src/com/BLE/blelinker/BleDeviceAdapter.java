package com.BLE.blelinker;

import java.util.List;

import com.BLE.blelinker.R.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BleDeviceAdapter extends ArrayAdapter<BleDevice> {

	int resource;
	
	public BleDeviceAdapter(Context context, int resource, List<BleDevice> devices){
		 
		super(context, resource, devices);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		return super.getView(position, convertView, parent);
		LinearLayout devicesView;
		
		BleDevice device = getItem(position);
		
		String devicename = device.getDevice_name();
		
		if(null == convertView){
			
			devicesView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater)getContext().getSystemService(inflater);
			li.inflate(resource, devicesView,true);
			
		}else{
			devicesView = (LinearLayout) convertView;
		}
		
		TextView device_name_tx = (TextView)devicesView.findViewById(R.id.device_name);
		
		device_name_tx.setText(devicename);
		 
		return devicesView;
	
	}
	
	
}
