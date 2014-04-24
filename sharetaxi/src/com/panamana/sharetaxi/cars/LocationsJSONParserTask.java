package com.panamana.sharetaxi.cars;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class LocationsJSONParserTask  extends AsyncTask<String, Integer, Map<String,Car> > {

	static final String TAG = "LocationJSONParserTask";
	
	/**
	 * Background
	 * @return 
	 */
	@Override
	protected Map<String,Car> doInBackground(String... jsonData) {
		Map<String,Car> cars = null;
		try {
			JSONObject jo = new JSONObject(jsonData[0]);
			// Parse
			cars = new LocationsJSONParser().parse(jo);
		} catch (JSONException joe){
			joe.printStackTrace();
//			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
//			return null;
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
//			return null;
		}
		return cars;
	}
	
}
