package com.example.wm_scanrssi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceScanActivity extends ListActivity {
private LeDeviceListAdapter mLeDeviceListAdapter;
private BluetoothAdapter mBluetoothAdapter;
private boolean mScanning;
private Handler mHandler;

private static final int REQUEST_ENABLE_BT = 1;
private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();


private static final long SCAN_PERIOD = 10000;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setTitle(R.string.title_devices);
    mHandler = new Handler();


    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        finish();
    }

    final BluetoothManager bluetoothManager =
            (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    mBluetoothAdapter = bluetoothManager.getAdapter();

    if (mBluetoothAdapter == null) {
        Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
        finish();
        return;
    }
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    if (!mScanning) {
        menu.findItem(R.id.menu_stop).setVisible(false);
        menu.findItem(R.id.menu_scan).setVisible(true);
     //  menu.findItem(R.id.menu_refresh).setActionView(null);
        menu.findItem(R.id.menu_refresh).setVisible(false);
    } else {
        menu.findItem(R.id.menu_stop).setVisible(true);
        menu.findItem(R.id.menu_scan).setVisible(false);
        menu.findItem(R.id.menu_refresh).setActionView(
                R.layout.actionbar_indeterminate_progress);
    }
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.menu_scan:
            mLeDeviceListAdapter.clear();
            scanLeDevice(true);
            break;
        case R.id.menu_stop:
            scanLeDevice(false);
            break;
    }
    return true;
}

@Override
protected void onResume() {
    super.onResume();

    if (!mBluetoothAdapter.isEnabled()) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    mLeDeviceListAdapter = new LeDeviceListAdapter();
    setListAdapter(mLeDeviceListAdapter);
    scanLeDevice(false);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
        finish();
        return;
    }
    super.onActivityResult(requestCode, resultCode, data);
}

@Override
protected void onPause() {
    super.onPause();
    scanLeDevice(false);
    mLeDeviceListAdapter.clear();
}

@SuppressWarnings("deprecation")
private void scanLeDevice(final boolean enable) {
    if (enable) {

    	mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                invalidateOptionsMenu();  // zi ding yi cai dan 
            }
        }, SCAN_PERIOD);
        mScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    } else {
        mScanning = false;
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
    invalidateOptionsMenu();
}


private class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices;
    private LayoutInflater mInflator;

    public LeDeviceListAdapter() {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
        mInflator = DeviceScanActivity.this.getLayoutInflater();
    }

    public void addDevice(BluetoothDevice device, int rssi) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
        rssiMap.put(device, rssi);
   //     rssiMap.get(device);
   //     System.out.println(device);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.deviceRssi = (TextView)view.findViewById(R.id.device_rssi);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BluetoothDevice device = mLeDevices.get(i);
      //  Log.e("uuid", device.getUuids().toString());
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
           viewHolder.deviceName.setText(R.string.unknown_device);
       viewHolder.deviceAddress.setText(device.getAddress());
       viewHolder.deviceRssi.setText(""+rssiMap.get(device)+" dBm");

        return view;
    }
}


private BluetoothAdapter.LeScanCallback mLeScanCallback =
        new BluetoothAdapter.LeScanCallback() {

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
             
            	//Log.e("RECORD", scanRecord.toString());
            	//Here is your Major value
                //Bytes 25 and 26 of the advertisement packet represent the major value
                int major = ((scanRecord[25]  & 0xFF) << 8)
                        | (scanRecord[26]  & 0xFF);

                //Bytes 27 and 28 of the advertisement packet represent the minor value
                int minor = ((scanRecord[27] & 0xFF) << 8)
                        | (scanRecord[28] & 0xFF);
               Log.e("major", major+"");
               Log.e("minor", minor+"");
                
                mLeDeviceListAdapter.addDevice(device, rssi);
                mLeDeviceListAdapter.notifyDataSetChanged();
            }
        });
    }
};

static class ViewHolder {
    TextView deviceName;
    TextView deviceAddress;
    TextView deviceRssi;
}
}
