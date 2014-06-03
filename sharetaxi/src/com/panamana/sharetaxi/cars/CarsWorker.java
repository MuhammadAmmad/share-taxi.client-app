package com.panamana.sharetaxi.cars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.panamana.sharetaxi.cars.locations.GetLocationsTask;
import com.panamana.sharetaxi.cars.locations.parser.LocationsJSONParserTask;
import com.panamana.sharetaxi.cars.locations.parser.LocationsJSONParser.LocationsJsonTags;
import com.panamana.sharetaxi.cars.objects.Car;
import com.panamana.sharetaxi.controller.activities.MapActivity;
import com.panamana.sharetaxi.model.maps.MapManager;
import com.panamana.sharetaxi.model.utils.ResourceUtils;

/**
 * Worker Thread: Draw cars over map.
 * @author naama
 */
public class CarsWorker extends Thread {

	// Constant:
	private static final String TAG = CarsWorker.class.getSimpleName();
	protected static final String MARKER_TITLE_PREFIX = "line";
	private final boolean DEBUG = false;
//	public static final String [] linesToHide = {};

	// Fields:
	public static Map<String, Car> cars = new HashMap<String, Car>();
	private String response = "";
	private Context context;
	private MapManager maps;

	// Constructor:
	public CarsWorker(Context c, MapManager maps) {
		this.context = c;
		this.maps = maps;
	}

	// Methods:
	
	public void run() {
		if (DEBUG)
			Log.i(TAG, "CarsWorker started");
		// removeCars();
		// 1. get Directions API response from line waypoints
		response = getLocations();
		if(DEBUG) {
			Log.i(TAG, "passed getLocations");
		}
		// 2. parse Directions API response to List<List<LatLng>> "routes"
		parseLocations(response);
		if(DEBUG) {
			Log.i(TAG, "passed parseLocations");
		}
		// 3. draw
		drawCars(cars);
	}

	/**
	 * adds car markers to map.
	 * @param cars
	 * @return
	 */
	private List<Car> drawCars(Map<String, Car> cars) {
		List<Car> carsList = new ArrayList<Car>(cars.values());

		for (final Car car : carsList) {
			try {
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {

						double localDirection = car.getLocalDirection();
						Log.i (TAG,"car localDirection: "+ localDirection);
						Marker marker = maps.addMarker(
								car.getLatLng(),
								MARKER_TITLE_PREFIX + car.getLineName() /*+ " " + localDirection*/,
								"Direction: "+car.getDirection() , 
								ResourceUtils.getImage(car.getIcon()),
								car.getID(), 
								MapActivity.linesToHide,
								car.getLocalDirection());
						car.setMarker(marker);
						if(DEBUG) Log.i(TAG, car.toString());
					}
				});
			} catch (IllegalStateException ile) {
				Log.e(TAG, ile.toString());
			}
		}
		return carsList;
	}

	/**
	 * parse location response JSON.
	 * @param response
	 */
	private void parseLocations(String response) {
		LocationsJSONParserTask parserTask = new LocationsJSONParserTask();
		// Invokes the thread for parsing the JSON data
		try {
			// wait get result from task
			// routes =
			parserTask.execute(response).get(10, TimeUnit.SECONDS);
			if(DEBUG) {
				Log.i(TAG, "parseLocations response:" + response);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
	
	/**
	 * get locations response
	 * @return
	 */

	private String getLocations() {
		GetLocationsTask glt = new GetLocationsTask();
		if (DEBUG)
			Log.i(TAG, "GetLocationsTask started");
		try {
			// wait get result from task
			response = glt.execute().get(10, TimeUnit.SECONDS);
			if (DEBUG)
				Log.i(TAG, "response: " + response.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		if (DEBUG)
			Log.i(TAG, "response: " + response);
		return response;
	}

	public static Car getCar (JSONObject jo) throws JSONException {
		return cars.get(jo.getString(LocationsJsonTags.ANDROIDID));
	}

}
