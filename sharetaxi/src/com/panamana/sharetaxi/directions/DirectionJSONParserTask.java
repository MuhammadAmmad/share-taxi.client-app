package com.panamana.sharetaxi.directions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.activities.MapActivity;

/**
 * Google Direction API response JSON parse AsyncTask
 * @author 
 */
class DirectionJSONParserTask extends AsyncTask<String, Integer, List<List<LatLng>>> {

	/**
	 * Background
	 */
	@Override
	protected List<List<LatLng>> doInBackground(String... jsonData) {
		List<List<LatLng>> routes = null;
		try {
			// Parse
			routes = new DirectionsJSONParser().parse(new JSONObject(jsonData[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routes;
	}

	/**
	 * On finish
	 */
	@Override
	protected void onPostExecute(List<List<LatLng>> routes) {
		PolylineOptions lineOptions = new PolylineOptions();
		// Traversing through all the routes
		for (List<LatLng> route : routes) {
			// Adding all the points in the route to LineOptions
			lineOptions.addAll(getAllPiontsInRoute(route));
		}
		// Drawing PolyLine in the Google Map for the i-th route
		MapActivity.drawPolyline(lineOptions);
	}

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