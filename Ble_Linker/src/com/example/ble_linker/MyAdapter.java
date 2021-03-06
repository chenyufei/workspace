package com.example.ble_linker;

 

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;

	public MyAdapter(Context context) {
		
		super();
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		
		
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	//****************************************final方法
//注意原本getView方法中的int position变量是非final的，现在改为final
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 ViewHolder holder = null;
		if (convertView == null) {
			
			holder=new ViewHolder();  
			
			//可以理解为从vlist获取view  之后把view返回给ListView
			
			convertView = mInflater.inflate(R.layout.device_item, null);
			holder.device_name = (TextView)convertView.findViewById(R.id.device_name);
			holder.connect_btn = (Button)convertView.findViewById(R.id.connect_btn);
			convertView.setTag(holder);				
		}else {				
			holder = (ViewHolder)convertView.getTag();
		}		
		
		holder.device_name.setText((String)MainActivity.mData.get(position).get("devicename")); 
		holder.connect_btn.setTag(position);
		//给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
		holder.connect_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo(position);					
			}
		});
		
		//holder.viewBtn.setOnClickListener(MyListener(position));
				
		return convertView;
	}
	
	
	
	public final class ViewHolder {
		public TextView device_name;
		public Button connect_btn;
	}
	
	
	public void showInfo(int position){
		  
		AlertDialog show = new AlertDialog.Builder(this.context)
		.setTitle("详情"+position)
		.setMessage("菜名：")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
		
		
	//	Toast.makeText(this.context, "Deleted Successfully!", Toast.LENGTH_LONG).show();
	}
	
}