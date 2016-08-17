package com.app.inside;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.app.inside.MainActivity;
import com.app.inside.R;
import com.app.inside.activityTabMap;
import com.app.inside.SearchGooglePlace.Place;
import com.app.inside.SearchGooglePlace.PlacesAutoCompleteAdapter;
import com.app.inside.SearchGooglePlace.PlacesAutoCompleteListener.PlacesAutoCompleteListeners;
import com.app.inside.favorite.FavoriteActivity;
import com.app.inside.help.CustomMap;
import com.app.inside.help.DialogMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener,OnItemClickListener{

	
    TextView StateName,CityName;
    double lat, lng;
    private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(R.drawable.ic_home);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.lay_actionbar, null);

        actionBar.setCustomView(v);
        AutoCompleteTextView textView = (AutoCompleteTextView) v
                .findViewById(R.id.edt_search);
        placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.list_item);
        placesAutoCompleteAdapter.setOnAutoCompleteListener(new PlacesAutoCompleteListeners() {
			
			@Override
			public void OnloadedPlace(Place place) {
				// TODO Auto-generated method stub
				Log.w("por", "Lat "+place.placeLat);
				
				mapPopup(place);
			}
		});
        
        textView.setAdapter(placesAutoCompleteAdapter);
        textView.setOnItemClickListener(this);
        
        
        StateName=(TextView)findViewById(R.id.StateName);// text to show latitude
        CityName=(TextView)findViewById(R.id.CityName);// text to show longitude
      
        turnGPSOn();
        getMyCurrentLocation();
        
        ImageButton bSearch = (ImageButton) findViewById(R.id.b_search_main);
        
		final EditText inSearch = (EditText) findViewById(R.id.input_search_main);

		bSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if (inSearch.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(),
							"��سһ�͹���˹觷���ͧ��ä���",
							Toast.LENGTH_LONG).show();
				} else {*/
					Intent intent = new Intent(MainActivity.this,
							activityListMap.class);
					intent.putExtra("Latitude", MyLat);
					intent.putExtra("Longitude", MyLong);
					intent.putExtra("strLocation", inSearch.getText()
							.toString());
					startActivity(intent);
				}
			//}
		});
		
      ImageView btnFood = (ImageView)findViewById(R.id.btnFood);
      btnFood.setOnClickListener(this);
      
      ImageView btnHotel = (ImageView)findViewById(R.id.btnHotel);
      btnHotel.setOnClickListener(this);
      
      ImageView btnMall = (ImageView)findViewById(R.id.btnMall);
      btnMall.setOnClickListener(this);
      
      ImageView btnGardent = (ImageView)findViewById(R.id.btn_gardent);
      btnGardent.setOnClickListener(this);
      
      ImageView btnTarvel = (ImageView)findViewById(R.id.btnTravel);
      btnTarvel.setOnClickListener(this);
      
      ImageView btnTranspot = (ImageView)findViewById(R.id.btnTranspot);
      btnTranspot.setOnClickListener(this);
      
      ImageView btnHotpital = (ImageView)findViewById(R.id.btnHotpital);
      btnHotpital.setOnClickListener(this);
      
      ImageView btnGovernment = (ImageView)findViewById(R.id.btnGovernment);
      btnGovernment.setOnClickListener(this);
      
      ImageView btnGass = (ImageView)findViewById(R.id.btnGass);
      btnGass.setOnClickListener(this);
      
    }

/** Method to turn on GPS **/
    public void turnGPSOn(){
        try
        {
       
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

       
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
        }
        catch (Exception e) {
           
        }
    }
// Method to turn off the GPS
    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
   
    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }
   
    /**
     * Check the type of GPS Provider available at that instance and
     * collect the location informations
     *
     * @Output Latitude and Longitude
     */
    void getMyCurrentLocation() {
       
       
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();
       
       
         try{gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
           try{network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

            //don't start listeners if no provider is enabled
            //if(!gps_enabled && !network_enabled)
                //return false;

            if(gps_enabled){
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
               
            }
           
           
            if(gps_enabled){
                location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               
               
            }
           
 
            if(network_enabled && location==null){
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
               
            }
       
       
            if(network_enabled && location==null)    {
                location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
           
            }
       
        if (location != null) {
           
            MyLat = location.getLatitude();
            MyLong = location.getLongitude();

       
        } else {
            Location loc= getLastKnownLocation(this);
            if (loc != null) {
               
                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();
               

            }
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.
       
        try
        {
// Getting address from found locations.
        Geocoder geocoder;
       
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
         addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

        StateNames = addresses.get(0).getAdminArea();
        CityNames  = addresses.get(0).getLocality();
        //CountryName = addresses.get(0).getCountryName();
        // you can get more details other than this . like country code, state code, etc.
       
       
        System.out.println(" StateName " + StateNames);
        System.out.println(" CityName " + CityNames);
        //System.out.println(" CountryName " + CountryName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
        StateName.setText(""+StateNames);
        CityName.setText(""+CityNames);
       
    }
   
    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
   
    private boolean gps_enabled=false;
    private boolean network_enabled=false;
    Location location;
   
    Double MyLat, MyLong;
    String CityNames="";
    String StateNames="";
    String CountryName="";
   
// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location. 

    public static Location getLastKnownLocation(Context context)
    {
        Location location = null;
        LocationManager locationmanager = (LocationManager)context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do
        {
            //System.out.println("---------------------------------------------------------------------");
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if(i != false && !locationmanager.isProviderEnabled(s))
                continue;
           // System.out.println("provider ===> "+s);
            Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
           // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
           // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;
    }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	Intent intent = new Intent(MainActivity.this,
			activityMap.class);
	
     
	switch (v.getId()) {
	case R.id.btnFood:
		intent.putExtra(getString(R.string.key_type_search), 1);
		startActivity(intent);
		
		break;
		
	case R.id.btnHotel:
		intent.putExtra(getString(R.string.key_type_search), 2);
		startActivity(intent);
		
		break;
		
	case R.id.btnMall:
		intent.putExtra(getString(R.string.key_type_search), 3);
		startActivity(intent);
		
		break;
		
	case R.id.btn_gardent:
		intent.putExtra(getString(R.string.key_type_search), 4);
		startActivity(intent);
		
		break;
		
	case R.id.btnTravel:
		intent.putExtra(getString(R.string.key_type_search), 5);
		startActivity(intent);
		
		break;
		
	case R.id.btnTranspot:
		intent.putExtra(getString(R.string.key_type_search), 6);
		startActivity(intent);
		
		break;
		
	case R.id.btnHotpital:
		intent.putExtra(getString(R.string.key_type_search), 7);
		startActivity(intent);
		
		break;
		
	case R.id.btnGovernment:
		intent.putExtra(getString(R.string.key_type_search), 8);
		startActivity(intent);
		
		break;
		
	case R.id.btnGass:
		intent.putExtra(getString(R.string.key_type_search), 9);
		startActivity(intent);
		
		break;

	default:
		break;
	}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_activity_actions, menu);
    return super.onCreateOptionsMenu(menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
        case R.id.action_favorite:
        	startActivity(new Intent(MainActivity.this,FavoriteActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
	// TODO Auto-generated method stub
	placesAutoCompleteAdapter.getPlace(i);
}

private void mapPopup(Place place){
	
//	FragmentManager fm = getSupportFragmentManager();
	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	 
	DialogMap dialogMap = new DialogMap(place);
	dialogMap.show(ft, "");
	
}
}