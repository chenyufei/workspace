package com.vic.chatclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vic.chatclient.ClientThread.ClientServiceCallback;

public class MainActivity extends ActionBarActivity {

	private Handler handler ;
	
	private Button conn_btn = null;
	private TextView address_tx = null;
	private Button send_btn = null;
	private EditText sendmsg_et = null;

	private ListView history_lt = null;

	private ArrayList<HashMap<String, String>> history_items = null;
	private ArrayAdapter<HashMap<String, String>> historyAdapter = null;
	private ServiceConnection mServerConnection = null;

	private ClientThread mClientThread = null;
	private ClientService mservice = null;
	


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

		conn_btn = (Button) findViewById(R.id.conn_btn);
		conn_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				handler = new Handler()
				{ 
					public void handleMessage(Message msg){
						if(msg.what == 0x123){
							displayMsg("server", msg.obj.toString());
						}
					}
					
				};
				mservice = new ClientService(handler);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new Thread(mservice).start();
					}
				});
				 
				
				mClientThread = new ClientThread();
				mClientThread.setCallBack(cb);
				mClientThread.start();
				String serverIP = mClientThread.getIpAddress();
				displayIP(serverIP);
			}
		});

		// sendMSG
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
							mClientThread.sendMsg(sendMsg);
							displayMsg("client", sendMsg);
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
				address_tx.setText("client disconnected");
			}
		});
	}
	
	
	
	private MsgCallBack cb = new MsgCallBack();
	public class MsgCallBack  implements ClientServiceCallback{
  
		@Override
		public void displayMsg(String Msg) {
			 MainActivity.this.displayMsg("server",Msg);
		}  
	}
}
