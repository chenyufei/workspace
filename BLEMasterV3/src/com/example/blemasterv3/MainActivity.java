package com.example.blemasterv3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.TabActivity;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.blemasterv3.ListAdapter.UUIDCallback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends TabActivity {

	private WampThread wampthread;
	// layout
	private SlidingMenu slidingMenu;
	// map
	private Button locate_btn = null;
	public String minor = null;
	public String major = null;

	public String sqlminor = null;
	public String sqlmajor = null;

	public String position = null;

	private Button location;

	private Timer timer;
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			
			relocate();
		}
	};;
	private Handler handler;

	//
	public static ListAdapter listAdapter;
	private Button scan_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initSlidingMenu();
		initTabUi();

		FragmentManager fm = getFragmentManager();
		DeviceListFragment devicelistFragment = (DeviceListFragment) fm
				.findFragmentById(R.id.DeviceListFragment);

		listAdapter = new ListAdapter(getApplicationContext(),
				R.layout.device_list_fragment, cb);

		devicelistFragment.setListAdapter(listAdapter);

		scan_btn = (Button) findViewById(R.id.scan_btn);
		scan_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listAdapter.onSetUp();
				listAdapter.scanLeDevice(true);
				


				timer = new Timer();
				// 参数：
				// 1000，延时1秒后执行。
				// 2000，每隔2秒执行1次task。
				timer.schedule(task, 2000, 3000);
			}
		});

		// Map
		

		location = (Button) findViewById(R.id.position_btn);

		locate_btn = (Button) findViewById(R.id.locate_btn);
		locate_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				wampthread = new WampThread();
				wampthread.start();
		 
				locate();

			}

		});

	}

	private void initTabUi() {

		// 获取tabhost组件
		TabHost tabHost = getTabHost();
		// 创建第一个tab页
		TabSpec scantab = tabHost.newTabSpec("scantab").setIndicator("Scanner")
				.setContent(R.id.tab01);
		// 添加第一个标签页
		tabHost.addTab(scantab);

		TabSpec tab2 = tabHost.newTabSpec("tab2").setIndicator("Map")
				.setContent(R.id.tab02);
		tabHost.addTab(tab2);

		TabSpec tab3 = tabHost.newTabSpec("tab3").setIndicator("tab3")
				.setContent(R.id.tab03);
		tabHost.addTab(tab3);

	}

	private void initSlidingMenu() {

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		// slidingMenu.setMenu(R.layout.menu_left);
		slidingMenu.setMenu(R.layout.menu_left_layout);// 零뒤寧몸（璘）꿋긋으
		// 零뒤랗몸(塘)꿋긋으,흼零LEFT_RIGHT친駕賈痰맡렘랬零塘꿋긋으
		// slidingMenu.setSecondaryMenu(R.layout.navigation_layout);
		// 쉥꿋긋으瀾젯돕Activity
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 零꿋긋으綾緞댕鬼
		// slidingMenu.setShadowWidth(10);
		// 零튤잼약잼
		slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		// 홍팁친駕，홍팁뺄땡떼옵댔역
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	private MyUUIDCallback cb = new MyUUIDCallback();

	public class MyUUIDCallback implements UUIDCallback {

		@Override
		public void setMajor(String major) {
			// TODO Auto-generated method stub
			MainActivity.this.major = major;
		}

		@Override
		public void setMinor(String minor) {
			// TODO Auto-generated method stub
			MainActivity.this.minor = minor;
		}

	}

	public void locate(){
 
		Log.e("blemajor", major + "");
		Log.e("bleminor", minor + "");
		
		if((null!=major)&&(null!=minor)){
		int iminor = Integer.parseInt(minor);
		
		 if(major.equals("907")){
		 switch(iminor){
		 case 101:
		 location.setX(40);
		 location.setY(20);
		 location.setVisibility(1);
		 break;
		 case 105:
		 location.setX(540);
		 location.setY(20);
		 location.setVisibility(1);
		 break;
		 case 501:
		 location.setX(20);
		 location.setY(500);
		 location.setVisibility(1);
		 break;
		 case 505:
		 location.setX(640);
		 location.setY(700);
		 location.setVisibility(1);
		 break;
		 }
		 } 
		}
	}

	public void relocate() {
		listAdapter.onSetUp();
		listAdapter.scanLeDevice(true);
		locate();

	}

	public class WampThread extends Thread {
		JSONArray jArray;
		String result = null;
		InputStream is = null;
		StringBuilder sb = null;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// http get
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://192.168.155.1/test.php");
				// HttpGet httpget = new HttpGet(
				// "http://166.104.112.17:80/test.php");
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection" + e.toString());
			}
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");

				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			int ct_id;
			sqlmajor = null;
			sqlminor = null;
			try {
				jArray = new JSONArray(result);
				JSONObject json_data = null;
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);
					ct_id = json_data.getInt("id");
					sqlmajor = json_data.getString("major");
					sqlminor = json_data.getString("minor");
					// tv.append(ct_name + " \n");
					Log.e("sqlmajor", sqlmajor);
					Log.e("sqlminor", sqlminor);
				}
			} catch (JSONException e1) {
				// Toast.makeText(getBaseContext(), "No City Found"
				// ,Toast.LENGTH_LONG).show();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

		}

	}

}
