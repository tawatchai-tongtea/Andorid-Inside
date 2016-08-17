package com.app.inside.adapter;

import java.util.ArrayList;

import com.app.inside.R;
import com.app.inside.object.Place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListSearchAdapter extends BaseAdapter{

	private ArrayList<Place>places ;
	private Context context;
	
	public ListSearchAdapter(Context context,ArrayList<Place>places){
		this.context = context;
		this.places = places;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return places.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView =  layoutInflater.inflate(R.layout.item_list_search, null);
			
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_name_search);
			viewHolder.txtAddress = (TextView)convertView.findViewById(R.id.txt_address_search);
			viewHolder.txtPhone = (TextView)convertView.findViewById(R.id.txt_phone_serach);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.txtName.setText(places.get(position).name);
		viewHolder.txtAddress.setText(places.get(position).address);
		viewHolder.txtPhone.setText(places.get(position).phone);
		
		
		return convertView;
	}

	public static class ViewHolder{
		public TextView txtName;
		public TextView txtAddress;
		public TextView txtPhone;
	}
}
