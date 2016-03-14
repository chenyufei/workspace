package com.ble.blescanner;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;


public class MainActivity extends Activity implements ScanDeviceFragment.OnNewDeviceAddedListener{
	
	public static ArrayAdapter<String> aa;
	public static ArrayList<String> devices;
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //��ȡ��Fragment������
        FragmentManager fm = getFragmentManager();
        DeviceListFragment devicelistFragment = 
        		(DeviceListFragment)fm.findFragmentById(R.id.devicelistfragment);
      
        //����device��arraylsit
        devices = new ArrayList<String>();
        
        //����ArrayAdapter����������󶨵�listview��
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices);
        
        //��ArrayAdapter�󶨵�listview��
        devicelistFragment.setListAdapter(aa);
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

	@Override
	public void onNewDeviceAdded(String newDevice) {
		// TODO Auto-generated method stub
		devices.add(newDevice);
		aa.notifyDataSetChanged();
	}
}
