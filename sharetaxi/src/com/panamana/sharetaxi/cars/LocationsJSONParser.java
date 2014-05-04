package com.panamana.sharetaxi.cars;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class LocationsJSONParser {

	public interface LocationsJsonTags {
//		static final public String DIRECTION = "direction";
		static final public String LINENUM = "lineNum";
		static final public String LONGITUDE = "longitude";
		static final public String ANDROIDID = "androidID";
		static final public String LATITUDE = "latitude";
		static final public String DATE = "date";
	}

	private static final String TAG = LocationsJSONParser.class.getSimpleName();
	private static final String POINTS = "points";

	public Map<String, Car> parse(JSONObject jObject) {
		// TODO Auto-generated method stub

		JSONArray jPoints = null;

		Log.i(TAG, "started parsing");
		try {
			jPoints = getPoints(jObject);
			/** Traversing all routes */
			for (int i = 0; i < jPoints.length(); i++) {
				
				JSONObject jo = jPoints.getJSONObject(i);
				String 	id        	=  jo.getString(LocationsJsonTags.ANDROIDID);           
				String 	date     	=  jo.getString(LocationsJsonTags.DATE);               
				String  line 		=  jo.getString(LocationsJsonTags.LINENUM);          
//				String 	direction 	=  jo.getString(LocationsJsonTags.DIRECTION);        
				LatLng  latlng    	=  new LatLng(                                       
		                              		jo.getDouble(LocationsJsonTags.LATITUDE), 
				                      		jo.getDouble(LocationsJsonTags.LONGITUDE));
				
				Car car = new Car(jPoints.getJSONObject(i));
				CarsWorker.cars.put(car.getID(), car);
				Log.i(TAG, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBcar entered Map: "+car.toString());
				Log.i(TAG, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBcars updated: "+ CarsWorker.cars.toString());
				
			}
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param jObject
	 * @return
	 * @throws JSONException
	 */
	private JSONArray getPoints(JSONObject jObject) throws JSONException {
		return jObject.getJSONArray(POINTS);
	}

}
