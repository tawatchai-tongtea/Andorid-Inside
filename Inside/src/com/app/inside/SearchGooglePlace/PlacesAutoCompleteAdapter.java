package com.app.inside.SearchGooglePlace;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.inside.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by narwhalstudio on 12/4/14 AD.
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
//    private ArrayList<String> resultList;
    private  Place place ;
    private ArrayList<PlaceDetail> placeDetailArrayList ;
    private PlacesAutoCompleteListener.PlacesAutoCompleteListeners placesAutoCompleteListener;

    private static final String LOG_TAG = "POR";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
//    private static final String API_KEY = "AIzaSyB0-9df4BW7CdcrZ9tvUsRgDUHld8KUImA";



    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return placeDetailArrayList.size();
    }

    @Override
    public String getItem(int index) {

        return placeDetailArrayList.get(index).namePlace;
    }

    public void getPlace(int index){

        new LoadDataPlace().execute(placeDetailArrayList.get(index).placeId,placeDetailArrayList.get(index).namePlace);
    }

    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		return super.getView(position, convertView, parent);
    	
    	try{
            
            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }
             
 
            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.txt_name);
            textViewItem.setText(placeDetailArrayList.get(position).namePlace);
             
             
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return convertView;
        
	}

	@Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    placeDetailArrayList = autocomplete(constraint.toString());

                    ArrayList<String>namePlace = new ArrayList<String>();

                    for(int i=0;i<placeDetailArrayList.size();i++){
                        namePlace.add(placeDetailArrayList.get(i).namePlace);
                    }

                    filterResults.values = namePlace;
                    filterResults.count = placeDetailArrayList.size();

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }





    //============= search

    private ArrayList<PlaceDetail> autocomplete(String input) {
        ArrayList<PlaceDetail> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
//            sb.append("?key=" + API_KEY);
            sb.append("?key="+getContext().getString(R.string.key_place));
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<PlaceDetail>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                PlaceDetail place = new PlaceDetail();

                place.namePlace = predsJsonArray.getJSONObject(i).getString("description");
                place.placeId = predsJsonArray.getJSONObject(i).getString("place_id");

                resultList.add(place);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }


    public void setOnAutoCompleteListener(PlacesAutoCompleteListener.PlacesAutoCompleteListeners listener){

        placesAutoCompleteListener = listener;
    }

    public Place getDetailPlace(String placeId,String namePlace) {

        Place places = new Place();

        String uri = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeId+"&key="+getContext().getString(R.string.key_place) ;


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {


            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return places;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return places;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {


            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject predsJsonArray = jsonObj.getJSONObject("result");


            places.placeLng = predsJsonArray.getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

            
            places.placeLat = predsJsonArray.getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

            places.placeName = namePlace;



        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return  places;
    }


    class LoadDataPlace extends AsyncTask<String, Integer, String> {

        @SuppressWarnings("null")
        @Override
        protected String doInBackground(String... params) {

            place = getDetailPlace(params[0],params[1]);

            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        public void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(String result) {

            if(placesAutoCompleteListener != null){
                placesAutoCompleteListener.OnloadedPlace(place);
            }

        }

    }

    class  PlaceDetail{
        String namePlace;
        String placeId;
    }



}
