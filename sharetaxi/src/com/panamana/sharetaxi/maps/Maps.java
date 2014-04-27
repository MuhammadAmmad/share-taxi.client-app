package com.panamana.sharetaxi.maps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.cars.CarsWorker;
import com.panamana.sharetaxi.directions.Line;

/**
 * Google Maps API manager class.
 * @author
 */
public class Maps {

	@SuppressWarnings("unused")
	private static final String TAG = Maps.class.getSimpleName();
	
	/*
	 * Maps API Help:
	 * https://developers.google.com/maps/documentation/android/start
	 */
	
	private static GoogleMap map;

	/**
	 * 
	 * @param context
	 */
	public static void createGoogleMap(Context context) {
		// Get a handle to the Map Fragment
		map = ((MapFragment)( (Activity)context).getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		// show my location
		map.setMyLocationEnabled(true);
	}
	
	/**
	 * 
	 * @param position
	 */
	public static void positionMap(LatLng position) {
		map.moveCamera(
				CameraUpdateFactory.newLatLngZoom(position, 13)
				);
		//map.animateCamera(CameraUpdateFactory.zoomIn());
	}
	
	/**
	 * position on my location 
	 */
	public static void positionMap() {

	    map.setMyLocationEnabled(true);

	    Location location = map.getMyLocation();

	    if (location != null) {
	        positionMap(new LatLng(location.getLatitude(),
	                location.getLongitude()));
	    }
	}

	/**
	 * 
	 * @param position
	 * @param title
	 * @param snippet
	 */
	public static Marker addMarker(LatLng position,String title,
			String snippet) {
		return map.addMarker(new MarkerOptions().title(title).snippet(snippet)
				.position(position));
	}
	
	public static Marker addMarker(LatLng position,String title,
			String snippet, BitmapDescriptor icon) {
		return map.addMarker(new MarkerOptions().title(title).snippet(snippet)
				.position(position).icon(icon));
	}
	

	public static void removeMarker(Marker marker) {
		marker.remove();
	}
/*

 */
	/**
	 * 
	 * @param points
	 */
	public static void drawPolyline(PolylineOptions lineOptions, int color, LatLng points[]) {
		for (int i = 0; i < points.length - 1; i++) {
			map.addPolyline(
					new PolylineOptions()
					.add(points[i], points[i + 1])
					.width(5).color(color));
		}
	}
	
	/**
	 * 
	 * @param lineOptions
	 */
	public static void drawPolyline(PolylineOptions lineOptions) {
		map.addPolyline(lineOptions);
	}
	
	public static void drawLine(Line line,Context context) {
		new LineWorker(line,context).start();
	}

	
	public static void drawCars(Context context) {
		// TODO Auto-generated method stub
		new CarsWorker(context).start();
		
		
	}
	
}
