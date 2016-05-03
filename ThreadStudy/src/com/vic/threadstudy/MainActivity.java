package com.vic.threadstudy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button start_btn = null;
	private MyThread mythread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		start_btn = (Button) findViewById(R.id.startbtn);

		start_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mythread = new MyThread();
				mythread.start();

			}
		});

	}

	public void display() {
		Toast.makeText(getApplicationContext(), "this", Toast.LENGTH_SHORT)
				.show();
	}

	public class MyThread extends Thread {

		private BufferedReader reader ;
		private InputStream mInputStream = null;
		private Socket clientSocket = null;
		private ServerSocket mServerScocket = null;
 

		public void run(){
			
			Log.d("ipaddress", getIpAddress());
			
    		Log.d("tedt", "aa"); 
    	//	MainActivity.this.display();
    		//Toast.makeText(getApplicationContext(), "this", Toast.LENGTH_SHORT).show();
    		try {
    			mServerScocket = new ServerSocket(8080);
    		while(true){
    			
    				clientSocket = mServerScocket.accept();
    				Log.d("tedt", "connect successfully"); 
    			
    			while(true){
    				String MSG = null;
    				reader =new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
    				MSG = reader.readLine();
    				Log.d("client msg", MSG);
    			}
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	}
	
	
		public String getIpAddress() {
			String ip = "";
			try {
				Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
						.getNetworkInterfaces();
				while (enumNetworkInterfaces.hasMoreElements()) {
					NetworkInterface networkInterface = enumNetworkInterfaces
							.nextElement();
					Enumeration<InetAddress> enumInetAddress = networkInterface
							.getInetAddresses();
					while (enumInetAddress.hasMoreElements()) {
						InetAddress inetAddress = enumInetAddress
								.nextElement();

						if (inetAddress.isSiteLocalAddress()) {
							ip += "Server running at : "
									+ inetAddress.getHostAddress();
						}
					}
				}

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ip += "Something Wrong! " + e.toString() + "\n";
			}
			return ip;
		}
	
	}

	
	
	
	
}
