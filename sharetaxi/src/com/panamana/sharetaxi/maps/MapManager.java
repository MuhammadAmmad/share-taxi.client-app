package com.panamana.sharetaxi.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.MultiKeyMap;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

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
import com.panamana.sharetaxi.cars.objects.Car;
import com.panamana.sharetaxi.lines.LINES;

/**
 * Google Maps API manager class.
 * Maps API Help:
 * https://developers.google.com/maps/documentation/android/start
 * @author
 */
public class MapManager {

	// Constants:
	private static final String TAG = MapManager.class.getSimpleName();
	public static final String [] LinesToHide = {LINES.LINE4,LINES.LINE4a};

	
	// Fields:
	// map //
	private GoogleMap map;
	// lists //
	public Map<String, Marker> markersMap = null;
	public static Map<String, PolylineOptions> polylineOptionsMap = null;
	public Map<String, Polyline> polylinesMap = null;

	// init:
	public MapManager(Context context) {
		markersMap = new HashMap<String, Marker>();
		polylineOptionsMap = new HashMap<String, PolylineOptions>();
		polylinesMap = new HashMap<String, Polyline>();
		createGoogleMap(context);
	}

	// Methods:
	
	// map //
	/**
	 * create map
	 * @param context
	 */
	private void createGoogleMap(Context context) {
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
	public void positionMap(LatLng position) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
		// map.animateCamera(CameraUpdateFactory.zoomIn());
	}

	/**
	 * position on my location
	 */
	public void positionMap() {

		map.setMyLocationEnabled(true);

		Location location = map.getMyLocation();

		if (location != null) {
			positionMap(new LatLng(location.getLatitude(),
					location.getLongitude()));
		}
	}

	// markers //

	/**
	 * adds cars markers to map
	 * @param position
	 * @param title
	 * @param snippet
	 * @param icon
	 * @param carId
	 * @return
	 */
//	public Marker addMarker(LatLng position, String title, String snippet, BitmapDescriptor icon, String carId) {
//		//
//		Marker marker = map.addMarker(new MarkerOptions().title(title)
//				.snippet(snippet).position(position).icon(icon));
//		//
//		Marker prevMarker = markersMap.get(carId);
//		if ( prevMarker != null) {
//			prevMarker.remove();
//		}
//		markersMap.put(carId, marker);
//		return marker;
//	}

	public Marker addMarker(LatLng position, String title, BitmapDescriptor icon, String carId, String ...linesToHide) {
		//
		for (int i=0; i<linesToHide.length; i++) {
			String lineNumber = linesToHide[i];
			if (title.equalsIgnoreCase(lineNumber)) {
				return null;
			}
		}
				Marker marker = map.addMarker(
						new MarkerOptions()
						.title(title)
						.position(position)
						.icon(icon));
				//
				Marker prevMarker = markersMap.get(carId);
				if ( prevMarker != null) {
					prevMarker.remove();
				}
				markersMap.put(carId, marker);
				return marker;
	}
	// polyline - routes //
	
	/**
	 * 
	 * @param points
	 */
//	public void drawPolyline(PolylineOptions lineOptions, int color,
//			LatLng points[]) {
//		for (int i = 0; i < points.length - 1; i++) {
//			map.addPolyline(new PolylineOptions().add(points[i], points[i + 1])
//					.width(5).color(color));
//		}
//	}

	public void addPolyline(String line,String ...linesToHide) {
		// add polyline to map
		for (int i=0; i<linesToHide.length; i++) {
			Log.i(TAG+"TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", "linesToHide[i]="+linesToHide[i]+" line="+line);
			if (line.equals(linesToHide[i])) {
				return;
			}
		}
		Polyline pol = map.addPolyline(polylineOptionsMap.get(line));
		polylinesMap.put(line, pol);			
	}

	public Polyline getPolyline(String line) {
		// get polyline from map
		return polylinesMap.get(line);
	}
//	
//	public List<Marker> getLineCars(String lineNum) {
//		List<Marker> lineCars = new ArrayList<Marker>(); 
//		for (Marker marker: markersMap.values()) {
//			if (marker.getTitle().equals(lineNum)) {
//				lineCars.add(marker);
//			}
//		}
//		return lineCars;
//	}

	// car markers //
	
	public Marker drawCars(Context context) {
		new CarsWorker(context,this).start();
		return null;

	}

	public void removeCars() {
		// TODO Auto-generated method stub
		for (Marker marker : markersMap.values()) {
			marker.remove();
		}
		markersMap.clear();

	}
}
