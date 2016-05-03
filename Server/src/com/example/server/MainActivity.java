package com.example.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static TextView mTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	//	Log.d("tedt", "aa");
		
		mTextView = (TextView) findViewById(R.id.textview);

		MyThread thread = new MyThread();
		thread.start();

	}

	private static boolean changeText(String Msg) {
		mTextView.setText(Msg);
		return true;
	}

	public class MyThread extends Thread {

		private InputStream mInputStream = null;
		private Socket clientSocket = null;
		private ServerSocket mServerScocket = null;
 
		
		 
		public void run(){
			Log.d("tedt", "aa");
			try {
				mServerScocket = new ServerSocket(8888);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				connect();
				Log.d("tedt", "aa");
				 getMsg();
 			}
		}
		
		
		public boolean connect() {

			try {
				clientSocket = mServerScocket.accept();
			 
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}

		public boolean getMsg() {

			try {
				mInputStream = clientSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte[] buf = new byte[512];
			String str = null;

			try {
				str = new String(buf, "GB2312").trim();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return MainActivity.changeText(str);
		}

 
	}

}
