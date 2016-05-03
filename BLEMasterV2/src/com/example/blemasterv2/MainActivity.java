package com.example.blemasterv2;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

 
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends Activity {
 
	private SlidingMenu slidingMenu;
	private Button scanbtn;
	private DeviceScanActivity scanservice;
	
	
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
        
        scanbtn = (Button)findViewById(R.id.scan_btn);
        
        scanbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scanservice = new DeviceScanActivity(getApplicationContext());
				scanservice.onSetUp();
				scanservice.scanLeDevice(true);
			}
		});
        
    }
     

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
    	switch(keyCode) {
    	
    	case KeyEvent.KEYCODE_MENU:
    		slidingMenu.toggle(true);
    		break;
    	default:
    		break;
    	}
    	return super.onKeyDown(keyCode, event);
	}


}
