package com.panamana.sharetaxi.cars;

import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

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
//		List<List<LatLng>> routes = null;
		private Context context;
		
		public CarsWorker(Context c){
			this.context=c;
		}
		
		public void run () {
			if(DEBUG) Log.i(TAG ,"CarsWorker started");
			// 1. get Directions API response from line waypoints
			response = getLocations();
			Log.i(TAG,"passed getLocations");
			// 2. parse Directions API response to List<List<LatLng>> "routes"
			parseLocations(response);
			Log.i(TAG,"passed parseLocations");
	        // 3. draw
	        drawCars();
		}

		private void drawCars() {
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

		private void parseLocations(String res) {
			LocationsJSONParserTask parserTask = new LocationsJSONParserTask();
	        // Invokes the thread for parsing the JSON data
	        try {
				// wait get result from task
//	        	routes = 
	        			parserTask.execute(res).get(10,TimeUnit.SECONDS);
	        			Log.i(TAG,"parseLocations response:"+ res);
			} catch (Exception e) {
				Log.e(TAG,e.toString());
			}
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
