package com.vic.chatclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Currency;
import java.util.Enumeration;
 
import android.app.Service;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ClientThread extends Thread{

	private Socket socket = null;
	private BufferedReader reader = null;
	 
	private RecClientThread recthread = null;
	
	public void run(){
		try{
			String Msg = null;  
			  	while(true){ 
			  		socket = RequestSocket("192.168.191.2", 8080);
			  		recthread = new RecClientThread(this.socket);
					break; 
			 	}
 				socket.close();
			  
		}catch (UnknownHostException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	public void setCallBack(ClientServiceCallback cb){
		this.msgCallback = cb;
	}
	
	
	
	
	private String ReceivedMsg() throws IOException{
		reader =  new BufferedReader( new InputStreamReader(socket.getInputStream()));
		String Msg = reader.readLine(); 
		return Msg;
	}
	

	private Socket RequestSocket(String host, int port) throws UnknownHostException, IOException{
		socket = new Socket(host, port);
		return socket;
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
						ip += inetAddress.getHostAddress();
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
	
	public void sendMsg(String msg){
		ChildClientThread sendThread = new ChildClientThread(msg);
		sendThread.start();
	}
	
	
	
	public class ChildClientThread extends Thread{
		
		public ChildClientThread(String msg) {
			// TODO Auto-generated constructor stub 
			this.msg = msg;
		}
		
		private BufferedWriter writer = null;
		private Socket socket = null; 
		public String msg = null;
		
		public void run(){
			 
			try{
				socket = RequestSocket("192.168.191.2", 8080);
				writer = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
				writer.write(msg);
				writer.flush();
 				Log.e("send to server", msg); 
 				socket.close();
				
			}catch (UnknownHostException e){
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}
			
		}  
	}
	
	

	public class RecClientThread extends Thread {

		private Socket socket = null;
		// private static final int PORT = 8080;
		private BufferedReader reader = null;

		private Socket s = null;
 
		public RecClientThread(Socket s) throws UnsupportedEncodingException,
				IOException {
			this.s = s;
			reader = new BufferedReader(new InputStreamReader(
					s.getInputStream(), "utf-8"));
		}

		public void run() {
  
			String content = null;
			while ((content = readFromServer()) != null) { 
				//Msg = ReceivedMsg();
				msgCallback.displayMsg(content);
				Log.d("from server", content);
			 
			}

		}
	}
		
		
	private String readFromServer() {
			// TODO Auto-generated method stub
			try {
				return reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}



	private ClientServiceCallback msgCallback = null;
	public interface ClientServiceCallback{ 
		public void displayMsg(String Msg); 
	}

}
