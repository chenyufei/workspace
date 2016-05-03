package com.example.socketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

	private Button startbtn;
	private Button sendbtn;
	private EditText inContent;
	private EditText outContent;

	private final static int PORT = 8080;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startbtn = (Button) findViewById(R.id.start_btn);
		sendbtn = (Button) findViewById(R.id.send_btn);
		inContent = (EditText) findViewById(R.id.in_et);
		outContent = (EditText) findViewById(R.id.out_et);

		startbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyThread clientThread = new MyThread();
				if(null != clientThread)
					clientThread.start();
			}

		});
	}
	
	public class MyThread extends Thread{
		 
		private BufferedWriter writer;
		private BufferedReader reader;
		
		public void run(){
			Socket server;
			String Msg = null;
			try{
				while(true){
					server = RequestSocket("192.168.191.2",PORT);  
					while(true){
						Msg = "ggkhgkhj";
						SendMsg(server,Msg);
						Log.e("MSG", Msg);
						break;
					}
					CloseSocket(server);
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	
		private void CloseSocket(Socket server) throws IOException {
			// TODO Auto-generated method stub
			//reader.close();
			writer.close();
			server.close();
		}

		//client get the Msg
		private Socket RequestSocket(String host, int port) throws UnknownHostException, IOException{
			Socket socket = new Socket(host, port);
			return socket;
		}
		//client send request
		private void SendMsg(Socket socket,String msg) throws IOException{
			//write the sending msg to the buffer
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			//formation
			writer.write(msg);
			writer.flush();
		}
		
		//receive msg
		private String ReceiveMsg(Socket socket) throws IOException{
			//readbuffer
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String Msg = reader.readLine();
			return Msg;
		}
		
	}
	

}
