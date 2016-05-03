package com.example.serversocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
	
	
	 
	//define the write buffer
	private BufferedWriter writer;
	private BufferedReader reader;
	private MyThread chatserver = null;

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
				 
				chatserver = new MyThread();
				 if(null != chatserver){
					 chatserver.start();
				 }
			}
		});

	} 
	
	public class MyThread extends Thread{
		private ServerSocket serverSocket = null;
		private Socket client = null;
		
		public void run(){
			String txt; 
			try{
				CreateSocket();
				while(true){
					
					ResponseSocket(); 
					while(true){
						txt = ReceiveMsg(client);
						Log.e("MSG", txt);
												
						if(true) break;
						
					}

					CloseSocket(client); 
				}
			
			}catch (IOException e){
				e.printStackTrace();
			}
			
			
		}
		private void CloseSocket(Socket client) throws IOException {
			// TODO Auto-generated method stub
			reader.close();
			//writer.close();
		//	serverSocket.close();
		}

		private void sendMsg(Socket client, String Msg) throws IOException {
			// TODO Auto-generated method stub
			writer = new BufferedWriter(
					new OutputStreamWriter(client.getOutputStream()));
			writer.write(Msg +"\n");
			writer.flush();
		}

		private String ReceiveMsg(Socket client) throws IOException {
			// TODO Auto-generated method stub
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String txt = reader.readLine();
			return txt ;
		}

		//receive msg
		private void ResponseSocket() throws IOException {
			// TODO Auto-generated method stub
			client = serverSocket.accept();
			String message = null;
			message += "#" + " from "
					+ client.getInetAddress() + ":"
					+ client.getPort() + "\n";

				Log.d("test", message);
			
		}

		private void CreateSocket() throws IOException {
			// TODO Auto-generated method stub
			serverSocket = new ServerSocket(PORT); 

		}
		
	}
	
	
}
