package com.app.inside;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.gdaplibrary.GooglePlaceSearch;
import app.akexorcist.gdaplibrary.GooglePlaceSearch.OnPlaceResponseListener;
import app.akexorcist.gdaplibrary.PlaceType;

public class activityDetail extends Activity {

	final String ApiKey = "AIzaSyD4_i3AJs30247FSenNduFgNJqVB6dFL_U";

	double latitude;
	double longitude;
	int radius = 1000;
	String type;
	String language = "th";
	String keyword;

	TextView textStatus;
	ListView listView;

	GooglePlaceSearch gp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_list);

		Bundle bundle = getIntent().getExtras();
		latitude = bundle.getDouble("Latitude");
		longitude = bundle.getDouble("Longitude");
		String strLocation = bundle.getString("strLocation");
		int status = bundle.getInt("status");
//		Toast.makeText(
//				getApplicationContext(),
//				String.valueOf(latitude) + "/" + String.valueOf(longitude)
//						+ "/" + strLocation + "/" + status, Toast.LENGTH_SHORT)
//				.show();

		keyword = strLocation;

		switch (status) {
		case 1:
			type = PlaceType.FOOD;
			break;
		case 2:
			type = PlaceType.LODGING;
			break;
		case 3:
			type = PlaceType.SHOPPING_MALL;
			break;
		case 4:
			type = PlaceType.PARK;
			break;
		case 5:
			type = PlaceType.TRAVEL_AGENCY;
			break;
		case 6:
			type = PlaceType.BUS_STATION;
			break;
		case 7:
			type = PlaceType.HOSPITAL;
			break;
		case 8:
			type = PlaceType.LOCAL_GOVERNMENT_OFFICE;
			break;
		case 9:
			type = PlaceType.GAS_STATION;
			break;
		}// Close Case
		textStatus = (TextView) findViewById(R.id.textStatus);

		listView = (ListView) findViewById(R.id.listDetail);

		gp = new GooglePlaceSearch(ApiKey);
		gp.setOnPlaceResponseListener(new OnPlaceResponseListener() {
			public void onResponse(String status,
					ArrayList<ContentValues> arr_data, Document doc) {
				textStatus.setText("ผลการค้นหา : " + status);

				if (status.equals(GooglePlaceSearch.STATUS_OK)) {
					final ArrayList<String> array = new ArrayList<String>();

					for (int i = 0; i < arr_data.size(); i++) {
						array.add("Name : "
								+ arr_data.get(i).getAsString(
										GooglePlaceSearch.PLACE_NAME)
								+ "\n"
								+ "Address : "
								+ arr_data.get(i).getAsString(
										GooglePlaceSearch.PLACE_ADDRESS)
								+ "\n"
								+ "Latitude : "
								+ arr_data.get(i).getAsString(
										GooglePlaceSearch.PLACE_LATITUDE)
								+ "\n"
								+ "Longitude : "
								+ arr_data.get(i).getAsString(
										GooglePlaceSearch.PLACE_LONGITUDE)
								+ "\n"
								+ "Phone Number : "
								+ arr_data.get(i).getAsString(
										GooglePlaceSearch.PLACE_PHONENUMBER));
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							activityDetail.this, R.layout.listview_text, array);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							
							Toast.makeText(getBaseContext(),
									array.get(position),
									Toast.LENGTH_SHORT).show();
							
							Intent intentMap = new Intent(activityDetail.this, activityTabMap.class);
							startActivity(intentMap);
				
						}
					});
				}
			}
		});

		gp.getNearby(latitude, longitude, radius, type, language, keyword);
		// gp.getTextSearch(keyword, type, false, language);
		// gp.getRadarSearch(latitude, longitude, radius, type, language, false,
		// keyword);
	}
}
