package com.vic.chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClientService extends Thread {

	private Socket socket = null;
	private Handler handler;
	
	public Handler revHandler = null;
	BufferedReader br =null;
	OutputStream os = null;
	
	public ClientService(Handler handler){
		this.handler = handler;
	}
	
	public void run(){

		try{
			socket = new Socket("192.168.191.2", 8080);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = socket.getOutputStream();
			new Thread(){
				public void run(){
					String content = null;
					try{
						while((content = br.readLine())!=null)
						{
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = content;
							handler.sendMessage(msg);
						}
					}
					catch (IOException e){
						e.printStackTrace();
					}
				}
			}.start();
			
//			
//			Looper.prepare();
//			revHandler = new Handler(){
//				public void hand
//			};
//			Looper.loop();
		}
		catch(SocketTimeoutException e){
			System.out.println("timeout");
		}
		catch (Exception ee){
			ee.printStackTrace();
		}
		
	}
	
	
	
	
}
