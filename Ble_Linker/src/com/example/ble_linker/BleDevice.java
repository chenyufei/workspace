package com.example.ble_linker;
 
	import android.bluetooth.BluetoothDevice;

	public class BleDevice {
		private BluetoothDevice mBleDevice;	
		private int mRssi;
		private byte[] mScanRecord;
		
		public BleDevice(BluetoothDevice bd, int rssi, byte[] sr)
		{
			mBleDevice = bd;
			mRssi = rssi;
			mScanRecord = sr;
		}
		
		public BluetoothDevice getBleDevice() {
			return mBleDevice;
		}
		public void setBleDevice(BluetoothDevice mBleDevice) {
			this.mBleDevice = mBleDevice;
		}
		public int getRssi() {
			return mRssi;
		}
		public void setRssi(int mRssi) {
			this.mRssi = mRssi;
		}
		public byte[] getScanRecord() {
			return mScanRecord;
		}
		public void setScanRecord(byte[] mScanRecord) {
			this.mScanRecord = mScanRecord;
		}
		
		

	}
 