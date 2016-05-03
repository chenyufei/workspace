package com.example.blemasterv3;



import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends Activity {
	
	//layout
	private SlidingMenu slidingMenu;
	
	
	//
	public static ListAdapter listAdapter;
	private Button scan_btn; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initSlidingMenu();
        
        
        FragmentManager fm = getFragmentManager();
        DeviceListFragment devicelistFragment = (DeviceListFragment)fm.findFragmentById(R.id.DeviceListFragment);
        
        listAdapter = new ListAdapter(getApplicationContext(), R.layout.device_list_fragment);
        
        devicelistFragment.setListAdapter(listAdapter);
        
        scan_btn = (Button)findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listAdapter.onSetUp();
				listAdapter.scanLeDevice(true);
			}
		});
                     
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
    
    
    private void initSlidingMenu(){
    	
    	 slidingMenu=new SlidingMenu(this); 
         slidingMenu.setMode(SlidingMenu.LEFT);
         //slidingMenu.setMenu(R.layout.menu_left);
         slidingMenu.setMenu(R.layout.menu_left_layout);//���õ�һ�����󣩲����
         //���õڶ���(��)�����,������LEFT_RIGHTģʽʹ�ø÷��������Ҳ����
         //slidingMenu.setSecondaryMenu(R.layout.navigation_layout);
         //�������ճ����Activity��
         slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
         //���ò������Ӱ��С
       //  slidingMenu.setShadowWidth(10);
         //����ƫ�����
         slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
         //ȫ��ģʽ��ȫ���������ɴ�
         slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    	
    }
    
    
       
}
