package com.panamana.sharetaxi.cars.locations.parser;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.panamana.sharetaxi.cars.CarsWorker;
import com.panamana.sharetaxi.cars.objects.Car;

/**
 * handle Locatio¯n response -> Car object.
 * @author naama
 */
public class LocationsJSONParser {

	// Constants:
	private static final String TAG = LocationsJSONParser.class.getSimpleName();
	private static final String POINTS = "points";
	private boolean DEBUG = false;

	// Interface:
	public interface LocationsJsonTags {

		static final public String LINENUM = "lineNum";
		static final public String LONGITUDE = "longitude";
		static final public String ANDROIDID = "androidID";
		static final public String LATITUDE = "latitude";
		static final public String DATE = "date";
		static final public String FREE_SEATS = "freeSeats";
	}

	// Methods:
	
	/**
	 * create cars from location JSON response.
	 * @param jObject
	 * @return
	 */
	public Map<String, Car> parse(JSONObject jObject) {
		JSONArray jPoints = null;
		if(DEBUG) {
			Log.i(TAG, "started parsing");
		}
		try {
			// cars locations from server
			jPoints = getPoints(jObject);
			for (int i = 0; i < jPoints.length(); i++) {
				// build car for each location JSON response
				Car car = new Car(jPoints.getJSONObject(i));
				if(DEBUG) Log.i(TAG,"1routeLocation"+Integer.toString(car.getIRouteLocation()));
				car.updateCarDirection();
				if(DEBUG) Log.i(TAG,"2routeLocation"+Integer.toString(car.getIRouteLocation()));
				if (car.getDirection() != "") {
					String direction = car.getDirection();
					if(DEBUG) Log.i(TAG,car.getDirection());
				}
					if(DEBUG) Log.i(TAG,"3routeLocation"+Integer.toString(car.getIRouteLocation()));
					CarsWorker.cars.put(car.getID(), car);
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
