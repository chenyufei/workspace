package com.vic.myserver;

import com.vic.myserver.Server.ServerCallBack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	
	private Button start_btn = null;
	private TextView state_tv = null;
	private TextView ipport_tv = null;
	private TextView rece_tv = null;
	private EditText send_et = null;
	private Button send_btn =null;
	
	
	private MyServerCallBack cb = new MyServerCallBack();
	
	private Server serverService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        start_btn = (Button)findViewById(R.id.start_btn);
        state_tv = (TextView)findViewById(R.id.state_tx);
        ipport_tv = (TextView)findViewById(R.id.ip_tx);
        rece_tv = (TextView)findViewById(R.id.rece_tv);
        send_et =(EditText)findViewById(R.id.sendmsg_et);
        send_btn = (Button)findViewById(R.id.send_btn);
        
        start_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				serverService = new Server(getApplicationContext());
				ipport_tv.setText(serverService.getIpAddress() +":"+ serverService.getServerPort());
				serverService.setServerCallBack(cb);
			}
		});
       
        
    } 
    public void displayMsg(final String msg){
    	
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

		    	rece_tv.setText(msg);
			}
		});
    	
    } 
    public class MyServerCallBack implements ServerCallBack{

		@Override
		public void displayNewMsg(String msg) {
			 
			MainActivity.this.displayMsg(msg);
		}
    	
    } 
    
}
