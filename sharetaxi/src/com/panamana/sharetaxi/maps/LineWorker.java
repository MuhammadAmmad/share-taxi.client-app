package com.panamana.sharetaxi.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.directions.DirectionJSONParserTask;
import com.panamana.sharetaxi.directions.Directions;
import com.panamana.sharetaxi.directions.GetDirectionsTask;
import com.panamana.sharetaxi.directions.Line;

/**
 * Worker Thread: Draw route over map from Line object.
 * @author naama
 */
class LineWorker extends Thread{
	
	// Fields:
	private static final String TAG = LineWorker.class.getSimpleName();
	private final boolean DEBUG = false;
	private Line line;
	private String response = "";
	List<List<LatLng>> routes = null;
	private Context context;
	
	public LineWorker(Line line, Context c){
		this.line=line;
		this.context=c;
	}
	
	public void run () {
		if(DEBUG) Log.i(TAG ,"lineWorker started");
		// 1. get Directions API response from line waypoints
		getDirections();
		// 2. parse Directions API response to List<List<LatLng>> "routes"
		parseDirections();
        // 3. draw
        drawRoute();
	}

	private void drawRoute() {
		if (routes != null) {
			// 1. build lineOptions for the waypoints
			final PolylineOptions lineOptions = new PolylineOptions();
			// Traversing through all the routes
			for (List<LatLng> route : routes) {
				// Adding all the points in the route to LineOptions
				try {
					lineOptions.addAll(getAllPiontsInRoute(route));	
				} catch (NullPointerException npe) {
					npe.printStackTrace();
					return;
				}
			}
			Log.i(TAG,"lineOptions:"+lineOptions.toString());
			
			// 2. Drawing PolyLine in the Google Map for the i-th route
			try {
				// set color and width
				lineOptions.width(line.getWidth()).color(line.getColor());
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// draw lineOptions on the map
						Maps.drawPolyline(lineOptions);
					}
				});
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}

	private void parseDirections() {
		DirectionJSONParserTask parserTask = new DirectionJSONParserTask();
        // Invokes the thread for parsing the JSON data
        try {
			// wait get result from task
        	routes = 
        			parserTask.execute(response).get(10,TimeUnit.SECONDS);
        	if(DEBUG) Log.i(TAG,"routes:"+routes.toString());
		} catch (Exception e) {
			Log.e(TAG,e.toString());
		}
	}

	private void getDirections() {
		GetDirectionsTask gdt = new GetDirectionsTask();
		String request = Directions.buildDirectionRequest(
				Directions.latlng2String(line.getStart()),
				Directions.latlng2String(line.getEnd()),
				Directions.latlng2String(line.getWaypoints()));
		if(DEBUG) Log.i(TAG,"request:"+request);
		if(DEBUG) Log.i(TAG,"GetDirectionsTask started");
		try {
			// wait get result from task
			response = gdt.execute(request).get(10,TimeUnit.SECONDS);
			if(DEBUG) Log.i(TAG,"response: "+response.toString());
		} catch (Exception e) {
			Log.e(TAG,e.toString());
		}
		if(DEBUG) Log.i(TAG,"response: "+response);
	}
	
	// helper:
	/**
	 * Fetch points in route i
	 * @param path
	 * @return
	 */
	private ArrayList<LatLng> getAllPiontsInRoute(List<LatLng> path) {
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		for (LatLng point : path) {
			points.add(point);
		}
		return points;
	}
}