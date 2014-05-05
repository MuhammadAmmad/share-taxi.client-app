package com.panamana.sharetaxi.cars.objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.panamana.sharetaxi.cars.locations.parser.LocationsJSONParser.LocationsJsonTags;

/**
 * this is the Car object that gets the JSON data from the locations server
 * response example:
 * "androidID": "1234", "date": 1399279938.0, "lineNum": "5", "longitude": "34.7833047", "latitude": "32.0987302"
 * @author naama
 */
public class Car {

	// Fields:
	private String mID;
	private String mTime;
	private String mDirection;
	private LatLng mLatLng;
	private String mLineName;
	private Marker mMarker;
	
	// Constructor:
	public Car (String ID, String time, String line, LatLng latlng ) {
		this.mID = ID;
		this.mTime = time;
		this.mLineName = line;
		this.mLatLng = latlng;
		this.mMarker = null;
	}
	public Car (JSONObject jo) throws JSONException {
		this(
			jo.getString(LocationsJsonTags.ANDROIDID),           
			jo.getString(LocationsJsonTags.DATE),               
			jo.getString(LocationsJsonTags.LINENUM),          
			new LatLng( jo.getDouble(LocationsJsonTags.LATITUDE), 
	                  	jo.getDouble(LocationsJsonTags.LONGITUDE))
			);
	}

	// Methods:
	
	public Marker getMarker() {
		return mMarker;
	}
	
	public void setMarker(Marker marker) {
		this.mMarker = marker;
	}
	/**
	 * prefix + "5" OR "4a" OR "4"
	 * @return
	 */
	public String getLineName() {
		return mLineName;
	}

	public void setLineName(String mLineName) {
		this.mLineName = mLineName;
	}

	/**
	 * i.e. 1234
	 * @return
	 */
	public String getID() {
		return mID;
	}

	/**
	 * i.e 1399279938.0
	 * @return
	 */
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
				+ mDirection + ", mLatLng=" + mLatLng + ", mLine=" + mLineName + ", mMarker=" + mMarker 
				+ "]";
	}
	
}
