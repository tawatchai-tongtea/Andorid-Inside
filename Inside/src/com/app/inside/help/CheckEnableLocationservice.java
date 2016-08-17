package com.app.inside.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class CheckEnableLocationservice {
	private static CheckEnableLocationservice instance;
	private static Context context;
public static CheckEnableLocationservice getinstance(Context con){
	context = con;
	if(instance == null){
		instance = new CheckEnableLocationservice();
	}
	return instance;
}
public boolean getstateLocationservice(){
	boolean state = false;
	LocationManager	lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)&& !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
		aLertDialog();
		state = false;
	}else{
		state = true;
	}
	return state;
	
}
public void aLertDialog() {
	final AlertDialog.Builder alert = new AlertDialog.Builder(context);
	alert.setTitle("Location service is disable");
	alert.setMessage("This app request to use location service");
	alert.setNegativeButton("Close", null);
	alert.setPositiveButton("Setting",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(intent);
				}
			});
	alert.show();
}
}
