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

	// Interface:
	public interface LocationsJsonTags {

		static final public String LINENUM = "lineNum";
		static final public String LONGITUDE = "longitude";
		static final public String ANDROIDID = "androidID";
		static final public String LATITUDE = "latitude";
		static final public String DATE = "date";
	}

	// Methods:
	
	/**
	 * create cars from location JSON response.
	 * @param jObject
	 * @return
	 */
	public Map<String, Car> parse(JSONObject jObject) {
		JSONArray jPoints = null;

		Log.i(TAG, "started parsing");
		try {
			jPoints = getPoints(jObject);
			for (int i = 0; i < jPoints.length(); i++) {
				// build car for each location JSON response
				Car car = new Car(jPoints.getJSONObject(i));
				car.updateCarDirection();
				if (car.getDirection() != null) {
					String direction = car.getDirection();
					Log.i(TAG+"YYYYYYYYYYYYYYYYYYYYYYYYYYYYY",car.getDirection());
				}
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
