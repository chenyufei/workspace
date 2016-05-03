package com.ble.blescanner;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
 

public class MainActivity extends Activity implements ScanDeviceFragment.OnNewDeviceAddedListener{
	
	private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();

	
	public static LeDeviceListAdapter aa;
	public static ArrayList<BluetoothDevice> devices;
	
	private SlidingMenu slidingMenu;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
        
        //获取该Fragment的引用
        FragmentManager fm = getFragmentManager();
        DeviceListFragment devicelistFragment = 
        		(DeviceListFragment)fm.findFragmentById(R.id.devicelistfragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	
	
	
	// Adapter for holding devices found through scanning.
		class LeDeviceListAdapter extends BaseAdapter {
			private ArrayList<BluetoothDevice> mLeDevices;
			private LayoutInflater mInflator;

			public LeDeviceListAdapter() {
				super();
				mLeDevices = new ArrayList<BluetoothDevice>();
				mInflator =  getLayoutInflater();
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
					view = mInflator.inflate(R.layout.listitem_device, null);
					viewHolder = new ViewHolder();
					viewHolder.deviceAddress = (TextView) view
							.findViewById(R.id.device_address);
					viewHolder.deviceName = (TextView) view
							.findViewById(R.id.device_name);
					viewHolder.deviceRssi = (TextView) view
							.findViewById(R.id.device_rssi);

					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}

				BluetoothDevice device = mLeDevices.get(i);
				final String deviceName = device.getName();
				if (deviceName != null && deviceName.length() > 0)
					viewHolder.deviceName.setText(deviceName);
				else
					viewHolder.deviceName.setText("unknown_device");
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

	@Override
	public void onNewDeviceAdded(String newDevice) {
		// TODO Auto-generated method stub
		
	}
	
	
}
