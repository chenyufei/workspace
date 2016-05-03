package com.example.thingswallet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements ActionBar.TabListener {

	private ActionBar actionbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);

		// set the nava mode
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// add tab page
		actionbar.addTab(actionbar.newTab().setText("Monitor")
				.setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText("setting")
				.setTabListener(this));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		
		switch(item.getItemId()){
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// new fragement
		Fragment fragment = new MonitorFragment();
		// new bundle to pass parameter to fragment
		// Bundle args = new Bundle();
		// args.putInt(key, value);
		FragmentTransaction fm = this.getFragmentManager().beginTransaction();
		fm.replace(R.id.container, fragment);
		fm.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
