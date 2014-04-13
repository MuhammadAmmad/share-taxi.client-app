package com.panamana.sharetaxi.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.directions.Directions;
import com.panamana.sharetaxi.maps.Maps;

/**
 * Main Activity.
 * @author 
 */
public class MapActivity extends Activity {

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		// create map
		Maps.createGoogleMap(this);
		// create points
		LatLng pointA = new LatLng(32.0540052,34.7801726);
		LatLng pointB = new LatLng(32.1050853,34.8032777);
		LatLng[] waypoints = {new LatLng(32.0550017,34.7754579)};
		// set map position
		Maps.positionMap(pointB);		
		// add point markers
		Maps.addMarker(pointA, "pointA", "Start");
		Maps.addMarker(pointB, "pointB", "Finish");
		Maps.addMarker(waypoints[0], "pointC", "Waypoint");
		// draw route
		Directions.drawRoute(pointA,pointB,waypoints);
	}
	
	/**
	 * 
	 * @param lineOptions
	 */
	public static void drawPolyline(PolylineOptions lineOptions) {
		lineOptions.color(Color.GREEN);
		lineOptions.width(5);
		Maps.drawPolyline(lineOptions);// TODO Auto-generated method stub
	}
}

