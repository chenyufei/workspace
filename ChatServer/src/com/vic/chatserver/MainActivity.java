package com.vic.chatserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
import com.vic.chatserver.ServerThread.ServerServiceCallback;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity{

	private Button start_btn = null;
	private TextView address_tx = null;
	
	private Button send_btn = null;
	private EditText sendmsg_et = null;
	
	private ListView history_lt = null;

	private ArrayList<HashMap<String, String>> history_items = null;
	private ArrayAdapter<HashMap<String, String>> historyAdapter = null; 

	private ServerThread mServerThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		address_tx = (TextView) findViewById(R.id.address_tx);

		history_lt = (ListView) findViewById(R.id.content_lv);
		history_items = new ArrayList<HashMap<String, String>>();

		historyAdapter = new ArrayAdapter<HashMap<String, String>>(this,
				android.R.layout.simple_list_item_1, history_items);
		history_lt.setAdapter(historyAdapter);

		start_btn = (Button) findViewById(R.id.start_btn);
		start_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// HashMap<String, String> msg = new HashMap<String, String>();
				// msg.put("server", "aaa");
				// history_items.add(msg);
				// historyAdapter.notifyDataSetChanged();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						mServerThread = new ServerThread();
						mServerThread.setCallBack(cb);
						mServerThread.start();
						String serverIP = mServerThread.getIpAddress();
						displayIP(serverIP); 
					}
				});
					}
				});
//send
		sendmsg_et = (EditText) findViewById(R.id.Msg_et);

		send_btn = (Button) findViewById(R.id.send_btn);
		send_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String sendMsg = sendmsg_et.getText().toString();
				Log.d("tip", sendMsg);
				if (null != sendMsg)
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {
								mServerThread.sendMsg(sendMsg);
								displayMsg("server",sendMsg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			}
		});
		

	}
    
	public void displayIP(final String ip) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				address_tx.setText(ip);
			}
		});
	}
	
	public void displayMsg(final String user,final String msg){ 
		runOnUiThread(new Runnable() { 
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 HashMap<String, String> item = new HashMap<String, String>();
				 item.put(user, msg);
				  history_items.add(item);
				  historyAdapter.notifyDataSetChanged();
			}
		});
	
	}

	public void disconnected() {
		runOnUiThread(new Runnable() { 
			@Override
			public void run() {
				// TODO Auto-generated method stub
				address_tx.setText("server disconnected");
			}
		});
	}  
	
	private MsgCallBack cb = new MsgCallBack();
	public class MsgCallBack  implements ServerServiceCallback{
  
		@Override
		public void displayMsg(String Msg) {
			 MainActivity.this.displayMsg("client",Msg);
		}  
	}
}
