package com.example.androidtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.net.ParseException;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class WampService extends Service{
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb = null;
	Handler handler;
	
	private WampThread wampthread;
	
	private final IBinder mBinder = new LocalBinder();
	public class LocalBinder extends Binder {
		WampService getService() {
			return WampService.this;
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	
	public static void makePostRequest() {

		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost("http://166.104.112.17/httppost.php"); // replace with
																// your url

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

		nameValuePair.add(new BasicNameValuePair("name", "fdsfsdf")); 

		// Encoding data

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// log exception
			e.printStackTrace();
		}

		// making request

		try {
			HttpResponse response = httpClient.execute(httpPost);
			// write response to log
			Log.d("Http Post Response:", response.toString());
		} catch (ClientProtocolException e) {
			// Log exception
			e.printStackTrace();
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
		}

	}

	public void connnectWamp(Handler handler){
		wampthread = new WampThread();
		wampthread.start();
		this.handler = handler;
	}

	public class WampThread extends Thread{
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	              // http get
	              try {
	                  HttpClient httpclient = new DefaultHttpClient();
	                  HttpGet httpget = new HttpGet(
	                         "http://192.168.155.1:80/test.php");
//	                  HttpGet httpget = new HttpGet(
//		                         "http://166.104.112.17:80/test.php");
	                  HttpResponse response = httpclient.execute(httpget);
	                  HttpEntity entity = response.getEntity();
	                  is = entity.getContent();
	              } catch (Exception e) {
	                  Log.e("log_tag", "Error in http connection" + e.toString());
	              }
	              // convert response to string
	              try {
	                  BufferedReader reader = new BufferedReader(
	                         new InputStreamReader(is, "iso-8859-1"), 8);
	                  sb = new StringBuilder();
	                  sb.append(reader.readLine() + "\n");
	 
	                  String line = "0";
	                  while ((line = reader.readLine()) != null) {
	                     sb.append(line + "\n");  
	                  }
	                  is.close();
	                  result = sb.toString();
	              } catch (Exception e) {
	                  Log.e("log_tag", "Error converting result " + e.toString());
	              }
	              int ct_id;
	              String ct_name;
	              try {
	                  jArray = new JSONArray(result);
	                  JSONObject json_data = null;
	                  for (int i = 0; i < jArray.length(); i++) {
	                     json_data = jArray.getJSONObject(i);
	                     ct_id = json_data.getInt("id");
	                     ct_name = json_data.getString("major");
	                     
	                     Message msg = new Message();
	                     msg.what = 0x123;
	                     msg.obj = ct_name;
	                     Log.e("NAME", ct_name);
	                     handler.sendMessage(msg);
	                     
	                     // tv.append(ct_name + " \n");
	                  }
	              } catch (JSONException e1) {
	                  // Toast.makeText(getBaseContext(), "No City Found"
	                  // ,Toast.LENGTH_LONG).show();
	              } catch (ParseException e1) {
	                  e1.printStackTrace();
	              }

			} 
			
		}
}
