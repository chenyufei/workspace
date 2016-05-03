package com.vic.chatserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ServerThread extends Thread {

	private static final int PORT = 8080;
	

	private BufferedWriter writer = null;
	// save all sockets
	private static ArrayList<Socket> socketList = new ArrayList<Socket>();

	private ServerThread currentThread = null;
	  
	public void setCallBack(ServerServiceCallback cb){
		this.msgCallback = cb;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket mServerSocket = null;
		try {
			mServerSocket = new ServerSocket(PORT, 10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			Socket s;
			try {
				s = mServerSocket.accept();
				socketList.add(s);
				new Thread(new ChildServerThread(s)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void sendMsg(String sendMsg) throws IOException {
		// TODO Auto-generated method stub
		for (Socket s : ServerThread.socketList) {
			OutputStream os = s.getOutputStream();
			os.write(sendMsg.getBytes("utf-8"));
		}
	}

	public ServerThread getService() {
		return ServerThread.this;
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
					InetAddress inetAddress = enumInetAddress.nextElement();
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

	public class ChildServerThread extends Thread {

		private ServerSocket server = null;
		// private static final int PORT = 8080;
		private BufferedReader reader = null;

		private Socket s = null;

		public ChildServerThread(Socket s) throws UnsupportedEncodingException,
				IOException {
			this.s = s;
			reader = new BufferedReader(new InputStreamReader(
					s.getInputStream(), "utf-8"));
		}

		public void run() {
  
			String content = null;
			while ((content = readFromClient()) != null) {
				Log.d("MSG", content);
				msgCallback.displayMsg(content); 
			}

		}

		private String readFromClient() {
			try {
				return reader.readLine();
			} catch (IOException e) {
				ServerThread.socketList.remove(s);
			}
			return null;
		}

		private void CreateSocket() throws IOException {
			// server = new ServerSocket(PORT,100);
		}

		private Socket ResponseSocket() throws IOException {
			Socket client = server.accept();
			return client;
		}

		private void CloseSocket(Socket socket) throws IOException {
			reader.close();
			writer.close();
			socket.close();
		}

		private void SendMsg(Socket socket, String Msg) throws IOException {
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			writer.write(Msg);
			writer.flush();
		}

		private String ReceiveMsg(Socket socket) throws IOException {
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String Msg = reader.readLine();
			return Msg;
		}
	}

	public ServerServiceCallback msgCallback = null; 
	public interface ServerServiceCallback {  
		public void displayMsg(String Msg); 
	}

}
