package com.panamana.sharetaxi.cars.locations;

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

public class GetLocationsTask extends AsyncTask<String, Void, String> {

	private static final String TAG = GetLocationsTask.class.getSimpleName();
	private static final boolean DEBUG = false;

	
	/**
	 * Background
	 * @author naama
	 */
	@Override
	protected String doInBackground(String...params) {
		URI uri = null;
		String url= "http://sharetaxi6.appspot.com/getLocations";
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
			if(DEBUG) {
				Log.e("get locations - doInBackground", e.toString());
			}
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
			Log.e("get locations - doInBackground", e.toString());
		}
		if(DEBUG) Log.i(TAG ,"response: "+response);
		return response;
	}

}

