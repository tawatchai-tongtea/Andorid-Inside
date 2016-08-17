package com.app.inside;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import app.akexorcist.gdaplibrary.GooglePlaceSearch;
import app.akexorcist.gdaplibrary.GooglePlaceSearch.OnPlaceResponseListener;
import app.akexorcist.gdaplibrary.PlaceType;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.app.inside.adapter.ListSearchAdapter;
import com.app.inside.object.Place;
import com.google.android.gms.maps.model.LatLng;


public class activityListMap extends Activity {

	double latitude;
	double longitude;
	int radius = 10000;
	String type;
	String language = "th";
	String keyword;
	int status;
	LocationManager lm;
    double lat, lng;

	TextView textStatus;
	ListView listView;

	GooglePlaceSearch gp;

	LayoutInflater inflater;
	AlertDialog.Builder popDialog;

	static String TAG = "INSIDE";
	private ArrayList<Place>places;
	private ListSearchAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_list);

		places = new ArrayList<Place>();
		
		Bundle bundle = getIntent().getExtras();
		latitude = bundle.getDouble("Latitude");
		longitude = bundle.getDouble("Longitude");
		String strLocation = bundle.getString("strLocation");
		status = bundle.getInt("status");

		keyword = strLocation;

		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

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

		gp = new GooglePlaceSearch(getString(R.string.key_place));
		
		if (status == 0) {
			gp.getTextSearch(keyword, null, false, language, latitude, longitude, radius);
		}
		//System.out.println(latitude);
		//System.out.println(longitude);
		
		if (status != 0) {
			gp.getNearby(latitude, longitude, radius, type, language, keyword);

		}
		
		gp.setOnPlaceResponseListener(new OnPlaceResponseListener() {
			public void onResponse(String status,
					ArrayList<ContentValues> arr_data, Document doc) {
				textStatus.setText("�š�ä��� : " + status);

				if (status.equals(GooglePlaceSearch.STATUS_OK)) {
					final ArrayList<String> array = new ArrayList<String>();
					final ArrayList<String> arrayLatitude = new ArrayList<String>();
					final ArrayList<String> arrayLongitude = new ArrayList<String>();

					for (int i = 0; i < arr_data.size(); i++) {
						
						Place place = new Place();
						place.name = arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_NAME);
						place.address = arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_ADDRESS);
						place.phone = arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_PHONENUMBER);
						place.latitude = Double.valueOf(arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_LATITUDE));
						place.longtitude = Double.valueOf(arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_LONGITUDE));
						
						places.add(place);
					}

//					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//							activityListMap.this, R.layout.listview_text, array);
					mAdapter  = new ListSearchAdapter(activityListMap.this, places);
					listView.setAdapter(mAdapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent, View v,
								final int position, long id) {
							
							View layout = inflater
									.inflate(
											R.layout.activity_dialog,
											(ViewGroup) findViewById(R.id.layout_popup));
							popDialog.setTitle("Popup Detail ");
							popDialog.setView(layout);
							
						
							
							popDialog.create();
							popDialog.show();
							

						}
					});
				}
			}
		});

		// gp.getTextSearch(keyword, type, false, language);
		// gp.getRadarSearch(latitude, longitude, radius, type, language, false,
		// keyword);
	}

}
