package com.panamana.sharetaxi.directions;

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
 * @author 
 */
class GetDirectionsTask extends AsyncTask<String, Void, String> {
	
	/**
	 * Background
	 */
	@Override
	protected String doInBackground(String... params) {
		URI uri = null;
		String url = params[0];
		HttpClient client = new DefaultHttpClient();
		Log.i("GetDirectionsTask","URL: "+url);
		try {
			uri = new URI(url);
		} catch (URISyntaxException use) {
			 Log.e("GetDirectionsTask","error URISyntaxException");
			 return null;
		} catch (RuntimeException rte) {
			rte.printStackTrace();
		}
		HttpGet request = new HttpGet(uri);
		HttpResponse response = null;
		StringBuffer results = new StringBuffer();
		String line = "";
		try {
			response = client.execute(request);
		} catch (Exception e) {
			Log.e("get directions - doInBackground", e.toString());
		}
		try {
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				results.append(line);
			}
		} catch (Exception e) {
			Log.e("get directions - onPostExecute", e.toString());
		}
		return new String(results);
	}

	/**
	 * On finish
	 */
	@Override
	protected void onPostExecute(String result) {
		Directions.handleDirectionReponse(result);
	}

}


//https://maps.googleapis.com/maps/api/directions/json?origin=32.0540052,34.7801726&destination=32.1050853,34.8032777&sensor=true&waypoints=via:32.0550017,34.7754579|via:32.056,34.7754579