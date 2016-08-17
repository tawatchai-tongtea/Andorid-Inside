package com.app.inside.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckConnectInternet {


    public static CheckConnectInternet check;
	
   public static CheckConnectInternet getInstance(){
	   if(check == null){
		   check = new CheckConnectInternet();
	   }
	   return check;
   }
	public  boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			aLertDialog(context);
			return false;
		}
	}

	public void aLertDialog(final Context context) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("No Internet Connection");
		alert.setMessage("This app request to connect internet");
		alert.setNegativeButton("Close", null);
		alert.setPositiveButton("Setting",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						context.startActivity(new Intent(
//								WifiManager.ACTION_PICK_WIFI_NETWORK));

                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                                context.startActivity(intent);
					}
				});
		alert.show();
	}
}
