package com.panamana.sharetaxi.maps;

import java.util.HashMap;
import java.util.Map;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.cars.CarsWorker;

/**
 * Google Maps API manager class.
 * 
 * @author
 */

public class Maps {

	public static Map<String, Marker> markersMap = null;
	public static Map<String, PolylineOptions> polylineOptionsMap = null;
	public static Map<String, Polyline> polylinesMap = null;

	static {
		markersMap = new HashMap<String, Marker>();
		polylineOptionsMap = new HashMap<String, PolylineOptions>();
		polylinesMap = new HashMap<String, Polyline>();
	}

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
		map = ((MapFragment) ((Activity) context).getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		// show my location
		map.setMyLocationEnabled(true);
	}

	/**
	 * 
	 * @param position
	 */
	public static void positionMap(LatLng position) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
		// map.animateCamera(CameraUpdateFactory.zoomIn());
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
	 * @param carId
	 * @param bitmapDescriptor
	 */
	public static Marker addMarker(LatLng position, String title,
			String snippet, BitmapDescriptor bitmapDescriptor) {

		Marker marker = map.addMarker(new MarkerOptions().title(title)
				.snippet(snippet).position(position));
		// put id,marker pair -> map
		return marker;
	}

	public static Marker addMarker(LatLng position, String title,
			String snippet, BitmapDescriptor icon, String carId) {
		Marker marker = map.addMarker(new MarkerOptions().title(title)
				.snippet(snippet).position(position).icon(icon));
		markersMap.put(carId, marker);
		return marker;
	}

	// public static void removeMarker(Marker marker) {
	// marker.remove();
	// }
	/*

 */
	/**
	 * 
	 * @param points
	 */
	public static void drawPolyline(PolylineOptions lineOptions, int color,
			LatLng points[]) {
		for (int i = 0; i < points.length - 1; i++) {
			map.addPolyline(new PolylineOptions().add(points[i], points[i + 1])
					.width(5).color(color));
		}
	}

	public static Marker drawCars(Context context) {
		// TODO Auto-generated method stub
		new CarsWorker(context).start();
		return null;

	}

	public static void removeCars() {
		// TODO Auto-generated method stub
		for (Marker marker : markersMap.values()) {
			marker.remove();
		}
		markersMap.clear();

	}

	public static void addPolyline(String line) {
		// add polyline to map
		Polyline pol = map.addPolyline(polylineOptionsMap.get(line));
		polylinesMap.put(line, pol);
	}

	public static Polyline getPolyline(String line) {
		// get polyline from map
		return polylinesMap.get(line);
	}
	
}
