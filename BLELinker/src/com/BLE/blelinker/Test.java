package com.BLE.blelinker;

import com.BLE.blelinker.ScanFragment.OnNewDeviceAddedListener;

public class Test {
	
	private OnNewDeviceAddedListener onNewDeviceAddedListener;
	private BleDevice ble;
	
	
	public void add(){
		ble = new BleDevice("no", "no", "no");
		for(int i =0; i<10;i++){
			ble.setDevice_name("device"+i);
			onNewDeviceAddedListener.onNewDeviceAdded(ble);
		}
	}
	
}
