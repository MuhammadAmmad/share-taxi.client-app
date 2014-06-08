package com.panamana.sharetaxi.lines.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.directions.DirectionsManager;
import com.panamana.sharetaxi.directions.parser.DirectionJSONParserTask;
import com.panamana.sharetaxi.directions.tasks.GetDirectionsTask;
import com.panamana.sharetaxi.lines.objects.Line;
import com.panamana.sharetaxi.model.maps.MapManager;
import com.panamana.sharetaxi.model.utils.LocationUtils;

/**
 * Worker Thread: Draw route over map from Line object.
 * 
 * @author naama
 */
class LineWorker extends Thread {

	// Fields:
	private static final String TAG = LineWorker.class.getSimpleName();
	private final boolean DEBUG = false;
	private Line line;
	private String response = "";
	List<List<LatLng>> routes = null;
	private MapManager maps;

	// constructor:

	public LineWorker(Line line, MapManager maps) {
		this.line = line;
		this.maps = maps;
	}

	// thread:

	public void run() {
		if (DEBUG)
			Log.i(TAG, "lineWorker started");
		// 1. get Directions API response from line waypoints
		getDirections();
		// 2. parse Directions API response to List<List<LatLng>> "routes"
		parseDirections();
		// new
		// 3. createPolylines
		PolylineOptions polyline = createPolylines();
		// 4. add polyline to list
		if (maps.polylineOptionsMap == null) {
			maps.polylineOptionsMap = new HashMap<String, PolylineOptions>();
		}
		maps.polylineOptionsMap.put(line.getName(), polyline);
		if (DEBUG) {
			Log.i(TAG, "polyline:" + polyline.toString());
		}
	}

	// methods:

	private PolylineOptions createPolylines() {
		final PolylineOptions lineOptions = new PolylineOptions();
		if (routes != null) {
			// 1. build lineOptions for the waypoints
			// Traversing through all the routes
			for (List<LatLng> route : routes) {
				// Adding all the points in the route to LineOptions
				try {
					lineOptions.addAll(getAllPiontsInRoute(route));
				} catch (NullPointerException npe) {
					npe.printStackTrace();
					return null;
				}
			}
			// set color and width
			lineOptions.width(line.getWidth()).color(line.getColor());
			if (DEBUG) {
				Log.i(TAG, "lineOptions:" + lineOptions.toString());
			}
		}
		return lineOptions;
	}

	private void parseDirections() {
		DirectionJSONParserTask parserTask = new DirectionJSONParserTask();
		// Invokes the thread for parsing the JSON data
		try {
			// wait get result from task
			routes = parserTask.execute(response).get(10, TimeUnit.SECONDS);
			if (DEBUG)
				Log.i(TAG, "routes:" + routes.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private void getDirections() {
		GetDirectionsTask gdt = new GetDirectionsTask();
		
		String request = DirectionsManager.buildDirectionRequest(
				LocationUtils.latlng2String(line.getStart()),
				LocationUtils.latlng2String(line.getEnd()),
				LocationUtils.latlng2String(line.getWaypoints()));
		if (DEBUG)
			Log.i(TAG, "request:" + request);
		if (DEBUG)
			Log.i(TAG, "GetDirectionsTask started");
		try {
			// wait get result from task
			response = gdt.execute(request).get(10, TimeUnit.SECONDS);
			if (DEBUG)
				Log.i(TAG, "response: " + response.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		if (DEBUG)
			Log.i(TAG, "response: " + response);
	}

	/**
	 * Fetch points in route i
	 * 
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
