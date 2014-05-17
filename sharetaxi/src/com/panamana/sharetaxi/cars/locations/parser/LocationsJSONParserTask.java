package com.panamana.sharetaxi.cars.locations.parser;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.panamana.sharetaxi.cars.objects.Car;

import android.os.AsyncTask;

public class LocationsJSONParserTask  extends AsyncTask<String, Integer, Map<String,Car> > {

	static final String TAG = "LocationJSONParserTask";
	
	/**
	 * Background
	 * @return 
	 */
	@Override
	protected Map<String,Car> doInBackground(String... jsonData) {
		try {
			JSONObject jo = new JSONObject(jsonData[0]);
			// Parse
			new LocationsJSONParser().parse(jo);
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
		return null;
	}
	
}
