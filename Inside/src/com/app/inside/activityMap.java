package com.app.inside;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import app.akexorcist.gdaplibrary.GooglePlaceSearch;
import app.akexorcist.gdaplibrary.GooglePlaceSearch.OnPlaceResponseListener;
import app.akexorcist.gdaplibrary.PlaceType;

import com.app.inside.help.CheckEnableLocationservice;
import com.app.inside.object.Place;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raw.arview.ARView;

public class activityMap extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
	

	public static ArrayList<Place>places;
	
	double latitude;
	double longitude;
	int radius = 1000;
	String type;
	String language = "th";
	String keyword;
	int status;
	
	static String TAG = "INSIDE";

	GooglePlaceSearch gp;

	 private GoogleApiClient mGoogleApiClient;
	 public static Location mLastLocation;
	 private ProgressDialog progressDialog;
	 private AlertDialog.Builder alert;
	 
	 private GoogleMap mMap;
	 private SupportMapFragment mapView;
	 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		 ActionBar actionBar = getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setTitle("Map View");
       
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading));
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		
		alert = new AlertDialog.Builder(activityMap.this);
		alert.setTitle(getString(R.string.title_dialog_error));
		alert.setMessage(getString(R.string.description_dialog_error));
		alert.setNegativeButton(getString(R.string.ok), null);
		
		
		 mapView = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
	     mMap = mapView.getMap();
	     mMap.setMyLocationEnabled(true);
	     mMap.getUiSettings().setZoomControlsEnabled(false);
	     mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub
				
				int index = Integer.parseInt(marker.getId().substring(1)) ;
				Log.w("por", "mk ID "+index);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					    Uri.parse("http://maps.google.com/maps?saddr="+mLastLocation.getLatitude()+","+mLastLocation.getLongitude()+"&daddr="+places.get(index).latitude+","+places.get(index).longtitude+""));
						startActivity(intent);
			}
		});
	        
		Bundle bundle = getIntent().getExtras();
//		latitude = bundle.getDouble("Latitude");
//		longitude = bundle.getDouble("Longitude");
//		String strLocation = bundle.getString("strLocation");
		status = bundle.getInt(getString(R.string.key_type_search));
//
//		keyword = strLocation;
//
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

		if(CheckEnableLocationservice.getinstance(this).getstateLocationservice()){
		
			try {

	            mGoogleApiClient = new GoogleApiClient.Builder(this)
	                    .addConnectionCallbacks(this)
	                    .addOnConnectionFailedListener(this)
	                    .addApi(LocationServices.API)
	                    .build();

	            Log.w("por","GoogleClient build");

	        } catch (Exception e){
	            Log.e("PTT","Error "+e.getMessage());
	        }

	        if(mGoogleApiClient != null){
	            mGoogleApiClient.connect();
	        }
	        
		}else{
			progressDialog.dismiss();
		}
		
		
        
		
//		if (status == 0) {
//			gp.getTextSearch(keyword);
//		}
//
//		if (status != 0) {
//			gp.getNearby(latitude, longitude, radius, type, language, keyword);
//
//		}
//		gp.setOnPlaceResponseListener(new OnPlaceResponseListener() {
//			public void onResponse(String status,
//					ArrayList<ContentValues> arr_data, Document doc) {
//
//				if (status.equals(GooglePlaceSearch.STATUS_OK)) {
//					final ArrayList<String> array = new ArrayList<String>();
//					final ArrayList<String> arrayLatitude = new ArrayList<String>();
//					final ArrayList<String> arrayLongitude = new ArrayList<String>();
//					final ArrayList<String> arrayNameLocation = new ArrayList<String>();
//					final ArrayList<String> arrayAddress = new ArrayList<String>();
//
//					for (int i = 0; i < arr_data.size(); i++) {
//						array.add("Name : "
//								+ arr_data.get(i).getAsString(
//										GooglePlaceSearch.PLACE_NAME)
//								+ "\n"
//								+ "Address : "
//								+ arr_data.get(i).getAsString(
//										GooglePlaceSearch.PLACE_ADDRESS)
//								+ "\n"
//								+ "Latitude : "
//								+ arr_data.get(i).getAsString(
//										GooglePlaceSearch.PLACE_LATITUDE)
//								+ "\n"
//								+ "Longitude : "
//								+ arr_data.get(i).getAsString(
//										GooglePlaceSearch.PLACE_LONGITUDE)
//								+ "\n"
//								+ "Phone Number : "
//								+ arr_data.get(i).getAsString(
//										GooglePlaceSearch.PLACE_PHONENUMBER));
//
//						Log.w(TAG, "Data "+array.get(i));
//						arrayLatitude.add(arr_data.get(i).getAsString(
//								GooglePlaceSearch.PLACE_LATITUDE));
//						arrayLongitude.add(arr_data.get(i).getAsString(
//								GooglePlaceSearch.PLACE_LONGITUDE));
//						arrayNameLocation.add(arr_data.get(i).getAsString(
//								GooglePlaceSearch.PLACE_NAME));
//						arrayAddress.add(arr_data.get(i).getAsString(
//								GooglePlaceSearch.PLACE_ADDRESS));
//					}
////					showMarker(arrayLatitude, arrayLongitude,
////							arrayNameLocation, arrayAddress);
//				}
//			}
//		});

		// gp.getNearby(latitude, longitude, radius, type, language, keyword);
		// gp.getTextSearch(keyword, type, false, language);
		// gp.getRadarSearch(latitude, longitude, radius, type, language, false,
		// keyword);
	}

	protected void showMarker(ArrayList<String> arrayLatitude,
			ArrayList<String> arrayLongitude,
			ArrayList<String> arrayNameLocation, ArrayList<String> arrayAddress) {
		// TODO Auto-generated method stub

		GoogleMap mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		for (int i = 0; i < arrayLatitude.size(); i++) {
			Log.d("====", "=====================");
			Log.d("Laa", arrayLatitude.get(i));
			Log.d("Loo", arrayLatitude.get(i));
			Log.d("====", "=====================");
			mMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(
									Double.parseDouble(arrayLatitude.get(i)),
									Double.parseDouble(arrayLongitude.get(i))))
					.title(arrayNameLocation.get(i))
					.snippet(arrayAddress.get(i)));
		}

		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(Double.parseDouble(arrayLatitude.get(0)), Double
						.parseDouble(arrayLongitude.get(0))), 14));
	}

	private void showMarkerOnMap(){
		
		if(mLastLocation != null){
			
			 LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
		     mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		     mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
		}
		
		for(int i=0;i<places.size();i++){
		
			Place place = places.get(i);
			Marker marker = mMap.addMarker(new MarkerOptions()
			                               .position(new LatLng(place.latitude, place.longtitude))
			                               .title(place.name)
			                               .snippet(place.address+" "+place.phone)
			                               .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
			marker.showInfoWindow();
			
			
		}
		
	}
	
	@Override
    public void onDestroy() {
       
        Log.w("por", "onDestroy service");
        if(mGoogleApiClient != null) {

            mGoogleApiClient.disconnect();
        }

        super.onDestroy();
    }
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		alert.show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
		 Log.w("por","onConnected service");

	        long interval = 1000*60;

	        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
	                mGoogleApiClient);
	        LocationServices.FusedLocationApi.requestLocationUpdates(
	                mGoogleApiClient, new LocationRequest().setInterval(interval).setFastestInterval(interval).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), this);

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		 Log.w("por","onLocationChanged service");
	        mLastLocation = location;

	        if (mLastLocation != null) {
	            Log.w("por", "curent service" + mLastLocation.getLatitude() + " , " + mLastLocation.getLongitude());

	            getData();
	            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, activityMap.this);
	        }
	        
	        
	        
	}
	
	private void getData(){
		
		progressDialog.show();
		
		gp = new GooglePlaceSearch(getString(R.string.key_place));
		gp.setOnPlaceResponseListener(new OnPlaceResponseListener() {
			
			@Override
			public void onResponse(String status, ArrayList<ContentValues> arr_data,
					Document doc) {
				// TODO Auto-generated method stub
				
				
				if(status.equals(GooglePlaceSearch.STATUS_OK)){
				
					places = new ArrayList<Place>();
					for (int i = 0; i < arr_data.size(); i++) {
						Log.w(TAG, "Name "+arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_LATITUDE));
						Place place = new Place();
						place.name = arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_NAME);
						place.address = arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_ADDRESS);
						place.phone = arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_PHONENUMBER);
						place.latitude = Double.valueOf(arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_LATITUDE));
						place.longtitude = Double.valueOf(arr_data.get(i).getAsString(GooglePlaceSearch.PLACE_LONGITUDE));
						
						places.add(place);
					}
					
					showMarkerOnMap();
					
				}else{
					
				    
					alert.show();
					
				}
				
				progressDialog.dismiss();
				
			}
			
		
		});
		
		gp.getNearby(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 5000, type);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	            return true;
	        case R.id.action_switch:
	        	Intent intent = new Intent(activityMap.this,ArActivity.class);
	        	intent.putExtra(getString(R.string.key_type_search), status);
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
