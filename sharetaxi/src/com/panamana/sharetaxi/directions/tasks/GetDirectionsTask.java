package com.panamana.sharetaxi.directions.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for Google Directions API HTTP GET request.
 * https://maps.googleapis.com/maps/api/directions/json?origin=32.0540052,34.7801726&destination=32.1050853,34.8032777&sensor=true&waypoints=via:32.0550017,34.7754579|via:32.056,34.7754579
 * @author naama 
 */
public class GetDirectionsTask extends AsyncTask<String, Void, String> {
	
	// constants:
	private static final String TAG = GetDirectionsTask.class.getSimpleName();
	private final boolean DEBUG = false;
	/**
	 * Background
	 * @author naama
	 */
	@Override
	protected String doInBackground(String... params) {
		URI uri = null;
		String url = params[0];
		if(DEBUG) Log.i(TAG ,"URL: "+url);
		try {
			uri = new URI(url);
		} catch (URISyntaxException use) {
			 Log.e(TAG,"error URISyntaxException");
			 return null;
		} catch (RuntimeException rte) {
			rte.printStackTrace();
		}
		if(DEBUG) Log.i(TAG ,"URI: "+uri);
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(uri);
		HttpResponse httpResponse = null;
		StringBuffer sb = new StringBuffer();
		String line = "";
		String response = "";
		try {
			httpResponse = client.execute(getRequest);
		} catch (Exception e) {
			Log.e("get directions - doInBackground", e.toString());
		}
		try {
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			response = new String(sb.toString());
			rd.close();
		} catch (Exception e) {
			Log.e("get directions - onPostExecute", e.toString());
		}
		if(DEBUG) Log.i(TAG ,"response: "+response);
		return response;
	}

}
