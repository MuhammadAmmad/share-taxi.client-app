package com.panamana.sharetaxi.addressSearch.tasks;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.panamana.sharetaxi.addressSearch.GeocodeJSONParser;
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.model.maps.MapManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/** A class to parse the Geocoding Places in non-ui thread */
public class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

    JSONObject jObject;
	private Context context;
	private MapManager maps;

	// Constructor:
	public ParserTask(Context c, MapManager maps) {
		this.context = c;
		this.maps = maps;
	}

    
    
    // Invoked by execute() method of this object
    @Override
    protected List<HashMap<String,String>> doInBackground(String... jsonData) {

        List<HashMap<String, String>> places = null;
        GeocodeJSONParser parser = new GeocodeJSONParser();

        try{
            jObject = new JSONObject(jsonData[0]);

            /** Getting the parsed data as a an ArrayList */
            places = parser.parse(jObject);

        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        return places;
    }
 // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(List<HashMap<String,String>> list){

        // Clears all the existing markers
    	// TODO
//        mMap.clear();

        for(int i=0;i<list.size();i++){

            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Getting a place from the places list
            HashMap<String, String> hmPlace = list.get(i);

            // Getting latitude of the place
            double lat = Double.parseDouble(hmPlace.get("lat"));

            // Getting longitude of the place
            double lng = Double.parseDouble(hmPlace.get("lng"));

            // Getting name
            String name = hmPlace.get("formatted_address");

            LatLng latLng = new LatLng(lat, lng);

            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker
            markerOptions.title(name);

            // Placing a marker on the touched position
            // TODO
            maps.addAddress(markerOptions);
            maps.positionMap(latLng);

            // Locate the first location
//            if(i==0)
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}