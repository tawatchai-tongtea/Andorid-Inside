package com.app.inside.favorite;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.inside.R;
import com.app.inside.adapter.ListSearchAdapter.ViewHolder;
import com.app.inside.object.Place;
import com.google.android.gms.internal.nu;

public class FavoriteAdapter extends BaseAdapter{

	private ArrayList<Place>places ;
	private Context context;
	private OnDeleteListener onDeleteListener;
	
	public interface OnDeleteListener{
		public void onDelete(int position);
	}
	
	public void setOndeleteListener(OnDeleteListener onDeleteListener){
		this.onDeleteListener = onDeleteListener;
	}
	
	public FavoriteAdapter(Context context,ArrayList<Place>places){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView =  layoutInflater.inflate(R.layout.item_history, null);
			
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_name_f);
			viewHolder.txtAddress = (TextView)convertView.findViewById(R.id.txt_address_f);
			viewHolder.txtPhone = (TextView)convertView.findViewById(R.id.txt_phone_f);
			viewHolder.btnDelete = (ImageView)convertView.findViewById(R.id.btn_delete);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.txtName.setText(places.get(position).name);
		viewHolder.txtAddress.setText(places.get(position).address);
		viewHolder.txtPhone.setText(places.get(position).phone);
		viewHolder.btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 AlertDialog.Builder builder = new AlertDialog.Builder(context);
                 builder.setTitle("Confirm");
                 builder.setMessage("คุณต้องการลบข้อมูล ใช่หรือไม่");
                 builder.setNegativeButton("ไม่", null);
                 builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                    	 if(onDeleteListener != null){
         					onDeleteListener.onDelete(position);
         				}
                     }
                 });
                 builder.show();
                 
				
			}
		});
		
		
		return convertView;
	}

	public static class ViewHolder{
		public TextView txtName;
		public TextView txtAddress;
		public TextView txtPhone;
		public ImageView btnDelete;
	}
}