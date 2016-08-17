package com.raw.arview;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.gdaplibrary.PlaceType;

import com.app.inside.MainActivity;
import com.app.inside.R;
import com.app.inside.activityMap;
import com.app.inside.object.Place;
import com.raw.utils.Camera;
import com.raw.utils.PaintUtils;
import com.raw.utils.RadarLines;


/**
 * 
 * Currently the markers are plotted with reference to line parallel to the earth surface.
 * We are working to include the elevation and height factors.
 * 
 * */


public class DataView {

	RelativeLayout.LayoutParams[] layoutParams;
	View[] view;
	LinearLayout[]layMaker;
	TextView[] txtName;
	TextView[] txtDistance;
	ImageView[] imgDirect;
	ImageView[]imgType;
	int[] plus ;
	
	
	
	int[] nextXofText ;
	ArrayList<Integer> 	nextYofText = new ArrayList<Integer>();

	double[] bearings;
	float angleToShift;
	float yPosition;
	Location currentLocation = new Location("provider");
	Location destinedLocation = new Location("provider");
	
	/** is the view Inited? */
	boolean isInit = false;
	boolean isDrawing = true;
	boolean isFirstEntry;
	Context _context;
	/** width and height of the view*/
	int width, height;
	android.hardware.Camera camera;

	float yawPrevious;
	float yaw = 0;
	float pitch = 0;
	float roll = 0;

	DisplayMetrics displayMetrics;
	RadarView radarPoints;

	RadarLines lrl = new RadarLines();
	RadarLines rrl = new RadarLines();
	float rx = 10, ry = 20;
	public float addX = 0, addY = 0;
	public float degreetopixelWidth;
	public float degreetopixelHeight;
	public float pixelstodp;
	public float bearing;

	public int[][] coordinateArray = new int[20][2];
	public int locationBlockWidth;
	public int locationBlockHeight;

	public float deltaX;
	public float deltaY;
	Bitmap bmp;

	private Location location;
	
	public DataView(Context ctx) {
		this._context = ctx;
	}


	public boolean isInited() {
		return isInit;
	}

	public void init(int type,final Context context,int widthInit, int heightInit, android.hardware.Camera camera, DisplayMetrics displayMetrics, RelativeLayout rel) {
		try {
			
			LayoutInflater layoutInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = new View[activityMap.places.size()];
			
			location = activityMap.mLastLocation;
			
			layoutParams = new RelativeLayout.LayoutParams[activityMap.places.size()];
			layMaker = new LinearLayout[activityMap.places.size()];
			txtName = new TextView[activityMap.places.size()];
			txtDistance = new TextView[activityMap.places.size()];
			nextXofText = new int[activityMap.places.size()];
			imgDirect = new ImageView[activityMap.places.size()];
			plus = new int[activityMap.places.size()];
			imgType = new ImageView[activityMap.places.size()];
			
			for(int i=0;i<activityMap.places.size();i++){
				
				final Place place = activityMap.places.get(i);
				
				plus[i] = new Random().nextInt(100);
				view[i] = layoutInflater.inflate(R.layout.lay_item_ar, null);
				layMaker[i] = (LinearLayout)view[i].findViewById(R.id.lay_marker);
				layMaker[i].setId(i);
				txtName[i] = (TextView)view[i].findViewById(R.id.txt_name);
				imgDirect[i] = (ImageView)view[i].findViewById(R.id.btn_direct);
				imgType[i] = (ImageView)view[i].findViewById(R.id.img_type);
				
				switch (type) {
				case 1:
					imgType[i].setImageResource(R.drawable.restauranticon);
					break;
				case 2:
					imgType[i].setImageResource(R.drawable.hot);
					break;
				case 3:
					imgType[i].setImageResource(R.drawable.sh);
					break;
				case 4:
					imgType[i].setImageResource(R.drawable.par);
					break;
				case 5:
					imgType[i].setImageResource(R.drawable.tra);
					break;
				case 6:
					imgType[i].setImageResource(R.drawable.bus);
					break;
				case 7:
					imgType[i].setImageResource(R.drawable.hos);
					break;
				case 8:
					imgType[i].setImageResource(R.drawable.gro);
					break;
				case 9:
					imgType[i].setImageResource(R.drawable.gas);
					break;
				}// Close Case
				
				txtDistance[i] = (TextView)view[i].findViewById(R.id.txt_distance);
				layoutParams[i] = new RelativeLayout.LayoutParams(R.dimen.width_ar_marker, R.dimen.height_ar_marker);
				layoutParams[i].setMargins(displayMetrics.widthPixels/2, displayMetrics.heightPixels/2, 0, 0);
				Location targetLocation = new Location("");
				targetLocation.setLatitude(place.latitude);
				targetLocation.setLongitude(place.longtitude);
				double distance = (location.distanceTo(targetLocation))/1000;
				DecimalFormat formatNumber = new DecimalFormat("0.00");
				txtName[i].setText(place.name);
				txtDistance[i].setText(""+formatNumber.format(distance)+" km.");
				view[i].setLayoutParams(layoutParams[i]);
				rel.addView(view[i]);


				imgType[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
							    Uri.parse("http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+place.latitude+","+place.longtitude+""));
								_context.startActivity(intent);
					}
				});

				imgDirect[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						boolean isSave = true;
						
						SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.key_favorite), Context.MODE_PRIVATE);
				        String history = prefs.getString(context.getString(R.string.key_favorite), "[]");
				        JSONArray jsonArray = new JSONArray();

				        try {
				            JSONArray jHistory = new JSONArray(history);

				            for(int i = 0;i<jHistory.length();i++){
				                jsonArray.put(jHistory.getJSONObject(i));
				                
				                if(jHistory.getJSONObject(i).getString("name").equals(place.name)){
				                	isSave = false;
				                }
				            }

				            if(isSave){
				            	 JSONObject jsonObject = new JSONObject();
						            jsonObject.put("name",place.name);
						            jsonObject.put("address",place.address);
						            jsonObject.put("phone",place.phone);
						            jsonObject.put("latitude",place.latitude);
						            jsonObject.put("longtitude",place.longtitude);
						            
						            jsonArray.put(jsonObject);
						            
						            Toast.makeText(context, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
				            }else{
				            	
				            	 AlertDialog.Builder builder = new AlertDialog.Builder(context);
				                 builder.setTitle("Fail");
				                 builder.setMessage("สถานที่นี้ถูกบันทึกแล้ว");
				                 builder.setNegativeButton("OK", null);
				                 builder.show();
				            }
				           


				        } catch (JSONException e) {
				            e.printStackTrace();
				        }

				        SharedPreferences.Editor editor = prefs.edit();
				        editor.putString(context.getString(R.string.key_favorite),""+jsonArray.toString());
				        editor.commit();
				        
				       
					}
				});

				layMaker[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v.getId() != -1) {
							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int count = 0;
							int index = 0;
							for (RelativeLayout.LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin, 
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
									count+=1;
								}
								index++;
							}
							
							if (count > 1) {
								
							}
//							Toast.makeText(_context, "Number of places here = "+count, Toast.LENGTH_SHORT).show();
							
							view[v.getId()].bringToFront();
//							Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}



			bmp = BitmapFactory.decodeResource(_context.getResources(), R.drawable.ic_home);

			this.displayMetrics = displayMetrics;
			this.degreetopixelWidth = this.displayMetrics.widthPixels / camera.getParameters().getHorizontalViewAngle();
			this.degreetopixelHeight = this.displayMetrics.heightPixels / camera.getParameters().getVerticalViewAngle();
			System.out.println("camera.getParameters().getHorizontalViewAngle()=="+camera.getParameters().getHorizontalViewAngle());

			bearings = new double[activityMap.places.size()];
			currentLocation.setLatitude(location.getLatitude());
			currentLocation.setLongitude(location.getLongitude());


			if(bearing < 0)
				bearing  = 360 + bearing;

			for(int i = 0; i <activityMap.places.size();i++){
				
				Place place = activityMap.places.get(i);
				
				destinedLocation.setLatitude(place.latitude);
				destinedLocation.setLongitude(place.longtitude);
				bearing = currentLocation.bearingTo(destinedLocation);

				if(bearing < 0){
					bearing  = 360 + bearing;
				}
				bearings[i] = bearing;

			}
			radarPoints = new RadarView(_context,this, bearings);
			this.camera = camera;
			width = widthInit;
			height = heightInit;
			
			lrl.set(0, -RadarView.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
			rrl.set(0, -RadarView.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		/*
		 * initialization is done, so dont call init() again.
		 * */
		isInit = true;
	}

	public void draw(PaintUtils dw, float yaw, float pitch, float roll) {


		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;

		// Draw Radar
		String	dirTxt = "";
		int bearing = (int) this.yaw; 
		int range = (int) (this.yaw / (360f / 16f));
		if (range == 15 || range == 0) dirTxt = "N"; 
		else if (range == 1 || range == 2) dirTxt = "NE"; 
		else if (range == 3 || range == 4) dirTxt = "E"; 
		else if (range == 5 || range == 6) dirTxt = "SE";
		else if (range == 7 || range == 8) dirTxt= "S"; 
		else if (range == 9 || range == 10) dirTxt = "SW"; 
		else if (range == 11 || range == 12) dirTxt = "W"; 
		else if (range == 13 || range == 14) dirTxt = "NW";


		radarPoints.view = this;

		dw.paintObj(radarPoints, rx+PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
		dw.setFill(false);
		dw.setColor(Color.argb(100,220,0,0));
		dw.paintLine( lrl.x, lrl.y, rx+RadarView.RADIUS, ry+RadarView.RADIUS); 
		dw.paintLine( rrl.x, rrl.y, rx+RadarView.RADIUS, ry+RadarView.RADIUS);
		dw.setColor(Color.rgb(255,255,255));
		dw.setFontSize(12);
		radarText(dw, "" + bearing + ((char) 176) + " " + dirTxt, rx + RadarView.RADIUS, ry - 5, true, false, -1);


		drawTextBlock(dw);
	}

	void drawPOI(PaintUtils dw, float yaw){
		if(isDrawing){
			dw.paintObj(radarPoints, rx+PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
			isDrawing = false;
		}
	}

	void radarText(PaintUtils dw, String txt, float x, float y, boolean bg, boolean isLocationBlock, int count) {

		float padw = 4, padh = 2;
		float w = dw.getTextWidth(txt) + padw * 2;
		float h;
		if(isLocationBlock){
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2+10;
		}else{
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		}
		if (bg) {

			if(isLocationBlock){
				
				layoutParams[count].setMargins((int)(x - w / 2 - 10), (int)(y - h / 2 - 10)+plus[count], 0, 0);
				layoutParams[count].height = R.dimen.height_ar_marker;
				layoutParams[count].width = R.dimen.width_ar_marker;
				view[count].setLayoutParams(layoutParams[count]);

			}else{
				dw.setColor(Color.rgb(0, 0, 0));
				dw.setFill(true);
				dw.paintRect((x - w / 2) + PaintUtils.XPADDING , (y - h / 2) + PaintUtils.YPADDING, w, h);
				pixelstodp = (padw + x - w / 2)/((displayMetrics.density)/160);
				dw.setColor(Color.rgb(255, 255, 255));
				dw.setFill(false);
				dw.paintText((padw + x -w/2)+PaintUtils.XPADDING, ((padh + dw.getTextAsc() + y - h / 2)) + PaintUtils.YPADDING,txt);
			}
		}

	}

	String checkTextToDisplay(String str){

		if(str.length()>15){
			str = str.substring(0, 15)+"...";
		}
		return str;

	}

	void drawTextBlock(PaintUtils dw){

		for(int i = 0; i<bearings.length;i++){
			
			Place place = activityMap.places.get(i);
			
			if(bearings[i]<0){

				if(this.pitch != 90){
					yPosition = (this.pitch - 90) * this.degreetopixelHeight+200;
				}else{
					yPosition = (float)this.height/2;
				}

				bearings[i] = 360 - bearings[i];
				angleToShift = (float)bearings[i] - this.yaw;
				nextXofText[i] = (int)(angleToShift*degreetopixelWidth);
				yawPrevious = this.yaw;
				isDrawing = true;
				radarText(dw, place.name, nextXofText[i], yPosition, true, true, i);
				coordinateArray[i][0] =  nextXofText[i];
				coordinateArray[i][1] =   (int)yPosition;

			}else{
				angleToShift = (float)bearings[i] - this.yaw;

				if(this.pitch != 90){
					yPosition = (this.pitch - 90) * this.degreetopixelHeight+200;
				}else{
					yPosition = (float)this.height/2;
				}


				nextXofText[i] = (int)((displayMetrics.widthPixels/2)+(angleToShift*degreetopixelWidth));
				if(Math.abs(coordinateArray[i][0] - nextXofText[i]) > 50){
					radarText(dw, place.name, (nextXofText[i]), yPosition, true, true, i);
					coordinateArray[i][0] =  (int)((displayMetrics.widthPixels/2)+(angleToShift*degreetopixelWidth));
					coordinateArray[i][1] =  (int)yPosition;

					isDrawing = true;
				}else{
					radarText(dw, place.name,coordinateArray[i][0],yPosition, true, true, i);
					isDrawing = false;
				}
			}
		}
	}
	
	public class NearbyPlacesList extends BaseAdapter{

		ArrayList<Integer> matchIDs = new ArrayList<Integer>();
		public NearbyPlacesList(ArrayList<Integer> matchID){
			matchIDs = matchID;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return matchIDs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}