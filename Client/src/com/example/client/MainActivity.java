package com.example.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private EditText mEditText = null;
	private Button connectbtn = null;
	private Button sendbtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mEditText = (EditText) findViewById(R.id.edittext);
		connectbtn = (Button) findViewById(R.id.connectbtn);
		sendbtn = (Button) findViewById(R.id.sendbutton);
		sendbtn.setEnabled(false);

		connectbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				MyThread mythread = new MyThread();
				mythread.start();

			}
		});
	}

	public class MyThread extends Thread {

		private Socket clientSocket = null;


		public void run() {
		 

			try {
				clientSocket = new Socket("192.168.191.3", 8080);
				
				while(true){
					try{
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
						writer.write("client msg");
						writer.flush();
					}catch (IOException e){
						e.printStackTrace();
					}

				}
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

}
