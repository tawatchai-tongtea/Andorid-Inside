package com.app.inside.favorite;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.inside.MainActivity;
import com.app.inside.R;
import com.app.inside.R.id;
import com.app.inside.R.layout;
import com.app.inside.R.menu;
import com.app.inside.favorite.FavoriteAdapter.OnDeleteListener;
import com.app.inside.object.Place;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavoriteActivity extends ActionBarActivity {

	ListView listView;
	FavoriteAdapter adapter;
    ArrayList<Place>places ;
    SharedPreferences prefs ;
    Location location;
    Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		ActionBar actionBar = getSupportActionBar();
	     actionBar.setDisplayHomeAsUpEnabled(true);
	     actionBar.setTitle("Favorite");
		context = this;
		
		location = MainActivity.getLastKnownLocation(this);
		
		prefs = getSharedPreferences(getString(R.string.key_favorite), Context.MODE_PRIVATE);
		
		getData();
		
		adapter = new FavoriteAdapter(this, places);
		adapter.setOndeleteListener(new OnDeleteListener() {
			
			@Override
			public void onDelete(int position) {
				// TODO Auto-generated method stub
				
				places.remove(position);
				
				SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.key_favorite), Context.MODE_PRIVATE);
				JSONArray jsonArray = new JSONArray();
		        try {
		            

		            for(int i = 0;i<places.size();i++){
		               
		            	Place place = places.get(i);
		            	
		            	JSONObject jsonObject = new JSONObject();
			            jsonObject.put("name",place.name);
			            jsonObject.put("address",place.address);
			            jsonObject.put("phone",place.phone);
			            jsonObject.put("latitude",place.latitude);
			            jsonObject.put("longtitude",place.longtitude);
			            
			            jsonArray.put(jsonObject);
		                
		            }


		        } catch (JSONException e) {
		            e.printStackTrace();
		        }

		        SharedPreferences.Editor editor = prefs.edit();
		        editor.putString(context.getString(R.string.key_favorite),""+jsonArray.toString());
		        editor.commit();
		        adapter.notifyDataSetChanged();
		        Toast.makeText(context, "ลบเรียบร้อย", Toast.LENGTH_SHORT).show();
			}
		});
		
		listView = (ListView) findViewById(R.id.list_favorite);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					    Uri.parse("http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+places.get(position).latitude+","+places.get(position).longtitude+""));
						startActivity(intent);
			}
		});
	}

	
	void getData(){
		
		 String history = prefs.getString(getString(R.string.key_favorite), "[]");
		 places = new ArrayList<Place>();
		 
	        try {
	            JSONArray jHistory = new JSONArray(history);

	            for(int i = 0;i<jHistory.length();i++){
	                JSONObject jsObject = jHistory.getJSONObject(i);
	                Place place = new Place();
	                place.name = jsObject.getString("name");
	                place.address = jsObject.getString("address");
	                place.phone = jsObject.getString("phone");
	                place.latitude = jsObject.getDouble("latitude");
	                place.longtitude = jsObject.getDouble("longtitude");
	                
	                places.add(place);
	            }


	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
