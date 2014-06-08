package com.panamana.sharetaxi.directions.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

/**
 * Google Direction API response JSON parse AsyncTask
 * @author 
 */
public class DirectionJSONParserTask extends AsyncTask<String, Integer, List<List<LatLng>>> {

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
	
}