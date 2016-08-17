package com.app.inside;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class activityTabMap extends Activity {

	private TabHost tabMap;
	private LocalActivityManager myLocalActivityManager;

	double latitude;
	double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);

		Bundle bundle = getIntent().getExtras();
		latitude = bundle.getDouble("Latitude");
		longitude = bundle.getDouble("Longitude");
		String strLocation = bundle.getString("strLocation");
		int status = bundle.getInt("status");

		tabMap = (TabHost) findViewById(R.id.tabhost);
		myLocalActivityManager = new LocalActivityManager(this, false);
		tabMap.setup(myLocalActivityManager);
		myLocalActivityManager.dispatchCreate(savedInstanceState);

		TabHost.TabSpec spec;

		Toast.makeText(
				getApplicationContext(),
				latitude + "//" + longitude + "//" + strLocation + "//"
						+ String.valueOf(status), Toast.LENGTH_SHORT).show();

		Intent mapList = new Intent(this, activityListMap.class);
		mapList.putExtra("Latitude", latitude);
		mapList.putExtra("Longitude", longitude);
		mapList.putExtra("strLocation", strLocation);
		mapList.putExtra("status", status);
		spec = tabMap
				.newTabSpec("mapList")
				.setIndicator("List Location",
						getResources().getDrawable(R.drawable.map49))
				.setContent(mapList);
		tabMap.addTab(spec);

		Intent map = new Intent(this, activityMap.class);
		map.putExtra("Latitude", latitude);
		map.putExtra("Longitude", longitude);
		map.putExtra("strLocation", strLocation);
		map.putExtra("status", status);
		spec = tabMap
				.newTabSpec("map")
				.setIndicator("Map",
						getResources().getDrawable(R.drawable.map49))
				.setContent(map);
		tabMap.addTab(spec);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		myLocalActivityManager.dispatchPause(isFinishing());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myLocalActivityManager.dispatchResume();
	}

}
