package com.app.inside.help;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.inside.MainActivity;
import com.app.inside.R;
import com.app.inside.SearchGooglePlace.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by tawatchaitongtea on 2/9/15 AD.
 */
public class CustomMap extends SupportMapFragment
{
    private Place place;

    public CustomMap()
    {
        super();
    }

    public static CustomMap newInstance(Place place)
    {
        CustomMap frag = new CustomMap();
        frag.place = place;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        initMap();
        return v;
    }

    private void initMap()
    {
        try {
            UiSettings settings = getMap().getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setMyLocationButtonEnabled(true);

            getMap().setMyLocationEnabled(true);
            getMap().getUiSettings().setZoomControlsEnabled(true);
            
            LatLng latLng = new LatLng(place.placeLat, place.placeLng);
            
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            MarkerOptions markerOtp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).title(place.placeName);
            Marker marker =  getMap().addMarker(markerOtp);
            marker.showInfoWindow();

            getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
					Location location = MainActivity.getLastKnownLocation(getActivity());
					
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				    Uri.parse("http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+place.placeLat+","+place.placeLng+""));
					startActivity(intent);
				}
			});
            

        }catch (Exception e){
            Log.e("por","error "+e.getMessage());
        }
    }
}