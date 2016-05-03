package com.vic.myserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

 

import android.content.Context;

public class Server {

	private ServerSocket server = null;
	private Socket client = null;
	private BufferedWriter writer = null;
	private BufferedReader reader = null;
	
	private Context context =null;
	private ServerCallBack mServerCallBack = null;
	
	private String serverIpAdd = null;
	static final int SERVER_PORT = 8080;
	
	private String msg = null;
	
	 
	public Server(Context main){
		this.context = main;
		Thread serverthread = new Thread(new ServerThread());
		serverthread.start();		
	}
	
	public String getServerIP(){
		return serverIpAdd;
	}
	
	public String getServerPort(){
		return Integer.toString(SERVER_PORT);
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
		
		serverIpAdd = ip;
		
		return ip;
	}
	
	public class ServerThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			 this.connect();
			 
			 try{
			
				 while(true){
					 client = server.accept();
					 while(true){
						reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String a = reader.readLine();
						if(null!=a);
						{
						msg += a;
						mServerCallBack.displayNewMsg(msg);
						break;
						}
					 }
					 client.close();
					 reader.close();
				 }
			 }catch (IOException e){
				 e.printStackTrace();
			 }
			 
			 
			  
		}
		
		public boolean connect(){
			
			try {
				server = new ServerSocket(SERVER_PORT);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} 
		
	}
	
	public void  setServerCallBack(ServerCallBack cb) {
        if (cb != null) {
        	mServerCallBack = cb;
        }
	}
	   public interface ServerCallBack{
		   public void displayNewMsg(String msg);
		   
	   }
	
	
}
