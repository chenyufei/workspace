package com.example.testwamp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.support.v7.app.ActionBarActivity;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb = null;

	private EditText tv = null;
 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b1 = (Button) findViewById(R.id.button1);

		tv = (EditText) findViewById(R.id.editView);
		
		b1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){

						public void run() {
							// TODO Auto-generated method stub
							ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				              // http get
				              try {
				                  HttpClient httpclient = new DefaultHttpClient();
				                  HttpGet httpget = new HttpGet(
				                         "http://192.168.155.1:80/test.php");
//				                  HttpGet httpget = new HttpGet(
//					                         "http://166.104.112.17:80/test.php");
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
							int ble_id;
							String ble_major;
							String ble_minor;

							try {
								jArray = new JSONArray(result);
								JSONObject json_data = null;
								for (int i = 0; i < jArray.length(); i++) {
									json_data = jArray.getJSONObject(i);
									ble_id = json_data.getInt("id");
									ble_major = json_data.getString("major");
									ble_minor = json_data.getString("minor");

									Log.e(ble_id+"", ble_major);
								}
							} catch (JSONException e1) {
								// Toast.makeText(getBaseContext(), "No City Found"
								// ,Toast.LENGTH_LONG).show();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

			}
		}.start();
				}
	});
 
	}
	

	private final Handler myhandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x123){
				System.out.println("------->" + msg.obj);
				tv.append(msg.obj + "\n");
			}
		}
	};
	
	
}
