package com.BLE.blelinker;

public class BleDevice {
	
	private String device_name;
	private String device_address;
	private String connect_status;
	 
	public BleDevice(String device_name, String device_address,
			String connect_status) {
		super();
		this.device_name = device_name;
		this.device_address = device_address;
		this.connect_status = connect_status;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_address() {
		return device_address;
	}
	public void setDevice_address(String device_address) {
		this.device_address = device_address;
	}
	public String getConnect_status() {
		return connect_status;
	}
	public void setConnect_status(String connect_status) {
		this.connect_status = connect_status;
	}
	
	
	
}
