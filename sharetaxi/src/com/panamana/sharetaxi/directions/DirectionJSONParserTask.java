package com.panamana.sharetaxi.directions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.activities.MapActivity;

/**
 * Google Direction API response JSON parse AsyncTask
 * @author 
 */
class DirectionJSONParserTask extends AsyncTask<String, Integer, List<List<LatLng>>> {

	static final String TAG = "DirectionJSONParserTask";
	
	/**
	 * Background
	 */
	@Override
	protected List<List<LatLng>> doInBackground(String... jsonData) {
		List<List<LatLng>> routes = null;
		try {
			JSONObject jo = new JSONObject(jsonData[0]);
			// Parse
			routes = new DirectionsJSONParser().parse(jo);
		} catch (JSONException joe){
			joe.printStackTrace();
			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			return null;
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
			if (routes == null) {
				Log.i (TAG,"onPostExecute: routes = null, quiting method");
				return;
			}
		
		for (List<LatLng> route : routes) {
			// Adding all the points in the route to LineOptions
			
			
			try {
				lineOptions.addAll(getAllPiontsInRoute(route));	
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
			
		}
		// Drawing PolyLine in the Google Map for the i-th route
		try {
			MapActivity.drawPolyline(lineOptions);	
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
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