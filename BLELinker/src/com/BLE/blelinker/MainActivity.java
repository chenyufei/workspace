package com.BLE.blelinker;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements ScanFragment.OnNewDeviceAddedListener{

	private BleDeviceAdapter aa;
	public static ArrayList<BleDevice>  bledevices;
	//public static BluetoothService  bleinstance;
	
	public static Test testa;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      //  testa = new Test();
        
        
        //获得该fragment的引用
        FragmentManager fm = getFragmentManager();
        ScanFragment scanfragment = (ScanFragment)fm.findFragmentById(R.id.scanfragment);
        
        BleListFragment blelistfragment = (BleListFragment)fm.findFragmentById(R.id.devicelistfragment);
        
        //创建待办项目的Arraylist
        bledevices = new ArrayList<BleDevice>();
        
        
        //创建deviceadapter 以将数组绑定到ListView
        int resID = R.layout.devicelist_fragment;
        aa = new BleDeviceAdapter(this, resID, bledevices);
        
        blelistfragment.setListAdapter(aa);
        
         //初始化ble
    //    bleinstance = new BluetoothService(this);
         
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
	public void onNewDeviceAdded(BleDevice newdevice) {
		// TODO Auto-generated method stub
		bledevices.add(newdevice);
		aa.notifyDataSetChanged();
	}
}
