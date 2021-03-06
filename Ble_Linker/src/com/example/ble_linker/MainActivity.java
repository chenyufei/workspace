package com.example.ble_linker;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private Button scan_btn;
	
	private BluetoothLE_Service bleservice;
	
	
	/** Called when the activity is first created. */
	public static List<Map<String, Object>> mData;
	private int flag;
	public static String title[]=new String[]{"菜名0","菜名1","菜名2","菜名3","菜名4","菜名5","菜名6","菜名7","菜名8","菜名9"};
	public static String info[]=new String[]{ "￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28","￥：28",};
	public static MyAdapter adapter;
	public ArrayList<String> devices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		scan_btn = (Button)findViewById(R.id.scan_btn);
		
		mData = getData();
		ListView listView = (ListView) findViewById(R.id.devices_list);
		adapter = new MyAdapter(this);
		listView.setAdapter(adapter);
		
		scan_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bleservice = new BluetoothLE_Service((Context)getApplicationContext(), MainActivity.this);
				bleservice.onSetupBle();
				bleservice.scanBleDevice(true);
			}
		});
		
		
	}
 
	//获取动态数组数据  可以由其他地方传来(json等)
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<title.length;i++){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title[i]);
		map.put("info", info[i]);
		list.add(map);
}

		return list;
	}

//	public void showInfo(int position){
//		  
//		new AlertDialog.Builder(this)
//		.setTitle("详情"+position)
//		.setMessage("菜名：")
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		})
//		.show();
//	}
	
	
}