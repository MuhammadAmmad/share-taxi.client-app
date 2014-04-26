package com.panamana.sharetaxi.cars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.Activity;

import org.json.JSONObject;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.maps.Maps;
import com.panamana.sharetaxi.utils.ResourceUtils;

import android.content.Context;
import android.util.Log;

/**
 * Worker Thread: Draw cars over map.
 * @author naama
 */

public class CarsWorker extends Thread{
		
		// Fields:
		private static final String TAG = CarsWorker.class.getSimpleName();
		private final boolean DEBUG = true;
		private String response = "";
		private Context context;
		
		public CarsWorker(Context c){
			this.context=c;
		}
		
		public void run () {
			if(DEBUG) Log.i(TAG ,"CarsWorker started");
			
			Map<String,Car> cars = null;
			// 1. get Directions API response from line waypoints
			response = getLocations();
			Log.i(TAG,"passed getLocations");
			// 2. parse Directions API response to List<List<LatLng>> "routes"
			cars = parseLocations(response);
			Log.i(TAG,"passed parseLocations");
	        // 3. draw
	        drawCars(cars);
		}

		private void drawCars(Map<String,Car> cars) {
			List <Car> carsList = new ArrayList<Car>(cars.values());
			
			for (final Car car : carsList) {
				try {
					((Activity)context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Log.i(TAG,car.toString());
							Maps.addMarker(
									car.getLatLng(),
									"Line"+car.getLine(),
									"Direction"+car.getDirection(),
									ResourceUtils.getImage(R.drawable.taxi_icon));
						}
					});
				} catch (IllegalStateException ile) {
					Log.e(TAG,ile.toString());
				}
			}
			//			if (routes != null) {
//				// 1. build lineOptions for the waypoints
//				final PolylineOptions lineOptions = new PolylineOptions();
//				// Traversing through all the routes
//				for (List<LatLng> route : routes) {
//					// Adding all the points in the route to LineOptions
//					try {
//						lineOptions.addAll(getAllPiontsInRoute(route));	
//					} catch (NullPointerException npe) {
//						npe.printStackTrace();
//						return;
//					}
//				}
//				Log.i(TAG,"lineOptions:"+lineOptions.toString());
//				
//				// 2. Drawing PolyLine in the Google Map for the i-th route
//				try {
//					// set color and width
//					lineOptions.width(line.getWidth()).color(line.getColor());
//					((Activity)context).runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							// draw lineOptions on the map
//							Maps.drawPolyline(lineOptions);
//						}
//					});
//				} catch (NullPointerException npe) {
//					npe.printStackTrace();
//				}
//			}
		}

		private Map<String,Car> parseLocations(String res) {
			LocationsJSONParserTask parserTask = new LocationsJSONParserTask();
			Map<String,Car> cars = null;;
	        // Invokes the thread for parsing the JSON data
	        try {
				// wait get result from task
//	        	routes = 
	        			cars = parserTask.execute(res).get(10,TimeUnit.SECONDS);
	        			Log.i(TAG,"parseLocations response:"+ res);
			} catch (Exception e) {
				Log.e(TAG,e.toString());
			}
			return cars;
		}

		private String getLocations() {
			GetLocationsTask glt = new GetLocationsTask();
			if(DEBUG) Log.i(TAG,"GetLocationsTask started");
			try {
				// wait get result from task
				response = glt.execute().get(10,TimeUnit.SECONDS);
				if(DEBUG) Log.i(TAG,"response: "+response.toString());
			} catch (Exception e) {
				Log.e(TAG,e.toString());
			}
			if(DEBUG) Log.i(TAG,"response: "+response);
			return response;
		}
		
}

