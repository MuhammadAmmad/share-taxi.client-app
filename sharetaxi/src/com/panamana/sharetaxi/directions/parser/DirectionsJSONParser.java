package com.panamana.sharetaxi.directions.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

/**
 * Parse JSON response from Google Direction API
 * @author 
 */
public class DirectionsJSONParser {

	// JSON parameters
	private static final String ROUTES = "routes";
	private static final String LEGS = "legs";
	private static final String STEPS = "steps";
	private static final String POLYLINE = "polyline";
	private static final String POINTS = "points";
	private static final String DURATION = "duration";

	/**
	 * Receives a JSONObject and returns a list of lists containing latitude and
	 * longitude
	 */
	public List<List<LatLng>> parse(JSONObject jObject, String time) {
		List<List<LatLng>> routes = new ArrayList<List<LatLng>>();
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;
		JSONObject jDuration = null;
		List<LatLng> list = null;
		List<LatLng> path = null;
		try {
			jRoutes = getRoutes(jObject);
			/** Traversing all routes */
			for (int i = 0; i < jRoutes.length(); i++) {
				jLegs = getLegs(jRoutes, i);
				path = new ArrayList<LatLng>();
				jDuration =  ((JSONObject) jLegs.getJSONObject(i)).getJSONObject(DURATION);
				time = getDuration(jDuration);
				/** Traversing all legs */
				for (int j = 0; j < jLegs.length(); j++) {
					jSteps = getSteps(jLegs, j);
					/** Traversing all steps */
					for (int k = 0; k < jSteps.length(); k++) {
						list = decodePoly(getPolyline(jSteps, k));
						/** Traversing all points */
						for (LatLng latLng : list) {
							path.add(latLng);
						}
					}
					routes.add(path);
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routes;
	}

	/**
	 * Method to decode PolyLine points Courtesy :
	 * jeffreysambells.com/2010/05/27
	 * decoding-PolyLines-from-google-maps-direction-API
	 */
	private List<LatLng> decodePoly(String encoded) {
		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		//
		while (index < len) {
			int b, shift = 0, result = 0;
			//
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			//
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
			poly.add(p);
		}
		return poly;
	}
	
	/**
	 * 
	 * @param jObject
	 * @return
	 * @throws JSONException
	 */
	private JSONArray getRoutes(JSONObject jObject) throws JSONException {
		return  jObject.getJSONArray(ROUTES);
	}

	/**
	 * 
	 * @param jLegs
	 * @param j
	 * @return
	 * @throws JSONException
	 */
	private JSONArray getSteps(JSONArray jLegs, int j) throws JSONException {
		return ((JSONObject) jLegs.get(j)).getJSONArray(STEPS);
	}

	/**
	 * 
	 * @param jRoutes
	 * @param i
	 * @return
	 * @throws JSONException
	 */
	private JSONArray getLegs(JSONArray jRoutes, int i) throws JSONException {
		return ((JSONObject) jRoutes.get(i)).getJSONArray(LEGS);
	}

	/**
	 * 
	 * @param jSteps
	 * @param k
	 * @return
	 * @throws JSONException
	 */
	private String getPolyline(JSONArray jSteps, int k) throws JSONException {
		return (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get(POLYLINE)).get(POINTS);
	}
	
	private String getDuration(JSONObject jDuration) throws JSONException {
		return (String)(jDuration.get("text"));
	}

}