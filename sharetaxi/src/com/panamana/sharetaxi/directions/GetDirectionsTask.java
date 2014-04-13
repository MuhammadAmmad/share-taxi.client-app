package com.panamana.sharetaxi.directions;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(params[0]);
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
