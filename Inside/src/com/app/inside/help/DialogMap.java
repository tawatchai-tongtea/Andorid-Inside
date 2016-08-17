package com.app.inside.help;

import com.app.inside.R;
import com.app.inside.SearchGooglePlace.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class DialogMap extends DialogFragment{

	private CustomMap mapView;
	private GoogleMap mMap;
	private Place place;
	
	public DialogMap (Place place){
		
		this.place = place;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.lay_popup_search_select, null);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		mapView = CustomMap.newInstance(place);
		FragmentManager fm = getChildFragmentManager();
		fm.beginTransaction().replace(R.id.lay_map,mapView).commit();
//		
		return view;
	}


	public void setLocation(LatLng latLng){
		
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
         MarkerOptions markerOtp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
         Marker marker =  mMap.addMarker(markerOtp);
	}
}
