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
        
        //获取该Fragment的引用
        FragmentManager fm = getFragmentManager();
        DeviceListFragment devicelistFragment = 
        		(DeviceListFragment)fm.findFragmentById(R.id.devicelistfragment);
      
        //创建device的arraylsit
        devices = new ArrayList<String>();
        
        //创建ArrayAdapter用来将数组绑定到listview上
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices);
        
        //将ArrayAdapter绑定到listview上
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
