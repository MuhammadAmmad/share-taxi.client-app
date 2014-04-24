package com.panamana.sharetaxi.cars;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.panamana.sharetaxi.cars.LocationsJSONParser.LocationsJsonTags;


// TODO:	change fields types to primitive (int,long)
public class Car {

	private String mID;
	private String mTime;
	private String mDirection;
	private LatLng mLatLng;
	private String mLine;
	
	public Car (String ID, String time, String line, String direction, LatLng latlng ) {
		this.mID = ID;
		this.mTime = time;
		this.mLine = line;
		this.mDirection = direction;
		this.mLatLng = latlng;
	}
	public Car (JSONObject jo) throws JSONException {
		this(
			jo.getString(LocationsJsonTags.ANDROIDID),           
			jo.getString(LocationsJsonTags.DATE),               
			jo.getString(LocationsJsonTags.LINENUM),          
			jo.getString(LocationsJsonTags.DIRECTION),        
			new LatLng( jo.getDouble(LocationsJsonTags.LATITUDE), 
	                  	jo.getDouble(LocationsJsonTags.LONGITUDE))
			);
	}

	public String getLine() {
		return mLine;
	}

	public void setLine(String mLine) {
		this.mLine = mLine;
	}

	public String getID() {
		return mID;
	}

	public String getTime() {
		return mTime;
	}

	public String getDirection() {
		return mDirection;
	}

	public LatLng getLatLng() {
		return mLatLng;
	}

	@Override
	public String toString() {
		return "Car [mID=" + mID + ", mTime=" + mTime + ", mDirection="
				+ mDirection + ", mLatLng=" + mLatLng + ", mLine=" + mLine
				+ "]";
	}

	
	
}
