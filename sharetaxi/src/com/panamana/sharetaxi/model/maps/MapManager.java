package com.panamana.sharetaxi.model.maps;

import java.util.HashMap;
import java.util.Map;

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
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.lines.objects.LineDirectionPair;
import com.panamana.sharetaxi.model.utils.Marker_Arrow;
import com.panamana.sharetaxi.model.utils.ResourceUtils;

/**
 * Google Maps API manager class.
 * Maps API Help:
 * https://developers.google.com/maps/documentation/android/start
 * @author
 */
public class MapManager {

	// Constants:
	private static final String TAG = MapManager.class.getSimpleName();
	private static final boolean DEBUG = false;

	
	// Fields:
	// map //
	private GoogleMap map;
	// lists //
	public static Map<String, Marker_Arrow> markersMap = null;
	public static Map<String, PolylineOptions> polylineOptionsMap = null;
	public static Map<String, Polyline> polylinesMap = null;

	// init:
	public MapManager(Context context) {
		markersMap = new HashMap<String, Marker_Arrow>();
		polylineOptionsMap = new HashMap<String, PolylineOptions>();
		polylinesMap = new HashMap<String, Polyline>();
		map = createGoogleMap(context);
	}

	// Methods:
	
	// map //
	/**
	 * create map
	 * @param context
	 */
	private GoogleMap createGoogleMap(Context context) {
		// Get a handle to the Map Fragment
		GoogleMap gmap = ((MapFragment) ((Activity) context).getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		// show my location
		gmap.setMyLocationEnabled(true);
		return gmap;
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

		if (map.getMyLocation() != null) {
			positionMap(new LatLng(
					map.getMyLocation().getLatitude(), 
					map.getMyLocation().getLongitude()));
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
	public Marker addMarker(LatLng position, String title, String freeSeats, String direction,
			BitmapDescriptor icon, String carId, Map<LineDirectionPair,Boolean> linesToHide, float localDirection) {
		
		Marker marker = null;
		Marker arrow = null;
		// build marker
		if (direction.split("Direction: ").length == 0 && (
			(linesToHide.get(LineDirectionPair.getPair(title, 
						LINES.getLine(title).getEndStations().getStartStation())))
				|| (linesToHide.get(LineDirectionPair.getPair(title, 
						LINES.getLine(title).getEndStations().getEndStation())))
						)
				||
				(direction.split("Direction: ").length > 0 && linesToHide
						.get(LineDirectionPair.getPair(title,
								direction.split("Direction: ")[1])) == true)) {
			// if car has no updated direction or should not be hided
			if (DEBUG) Log.i(TAG,"title="+title+" direction="+direction);
				marker = map.addMarker(
						new MarkerOptions()
						.title(title)
						.snippet("Free Seats: "+ freeSeats)
						.position(position)
						.icon(icon));
				arrow = map.addMarker(
						new MarkerOptions()
						.position(position)
						.icon(ResourceUtils
						.getImage(R.drawable.marker_arrow)));
				arrow.setRotation(localDirection);
				marker.setAnchor(0.5f, 0.5f);
				// get previous marker
				Marker_Arrow prevMarker = markersMap.get(carId);
				
				// add marker to map
				markersMap.put(carId, new Marker_Arrow(marker,arrow) );
//				markersMap.put(carId, marker);
				if (prevMarker != null) {
					// got previous marker
					prevMarker.remove();
				}
		}
		return marker;
	}

	// polyline - routes //

	public void addPolyline(String line,Map<LineDirectionPair,Boolean> linesToHide) {
		// add polyline to map
		// if both directions are in linesToHide then hide
		if (DEBUG) Log.i(TAG,"line="+line+" direction="+LINES.getLine(line)
				.getEndStations().getStartStation()+" linesToHide= "+linesToHide.toString());
		if (
				linesToHide.get(LineDirectionPair.getPair(line, 
				LINES.getLine(line).getEndStations().getStartStation())) 
				== true ||
				linesToHide.get(LineDirectionPair.getPair(line, 
				LINES.getLine(line).getEndStations().getEndStation())) 
				== true 
				) {
			Polyline pol = map.addPolyline(polylineOptionsMap.get(line));
			if (polylinesMap.get(line) != null) {
				Polyline prev = polylinesMap.get(line);
				prev.remove();
			}
			polylinesMap.put(line, pol);			
		}
	}

	public Polyline getPolyline(String line) {
		// get polyline from map
		return polylinesMap.get(line);
	}

	// car markers //
	
	public Marker drawCars(Context context) {
		new CarsWorker(context,this).start();
		return null;

	}

	public static void removeCars() {
		// TODO Auto-generated method stub
		for (Marker_Arrow marker : markersMap.values()) {
			marker.remove();
		}
		markersMap.clear();
	}

	public void HidePolylines(Map<LineDirectionPair,Boolean> linesToHide) {
		if(DEBUG) Log.i(TAG,linesToHide.toString()); 
		for (LineDirectionPair lineDirection : linesToHide.keySet()) {
			if (polylinesMap.get(lineDirection.getLine()) != null) {
				if (linesToHide.get(lineDirection) == false) {
					if ( linesToHide.get(LineDirectionPair.getPair(
							lineDirection.getLine(),
							LINES.getOppositeDirection( lineDirection.getLine()
									, lineDirection.getDirection()))) == false) {
						if (DEBUG) Log.i(TAG,"lineDirection.getLine()= "+ lineDirection.getLine() ) ;
						polylinesMap.get(lineDirection.getLine()).setVisible(false);
					}
				}
			}
		}
	}

	public void addAddress(MarkerOptions markerOptions) {
		Marker marker = map.addMarker(markerOptions);
		
	}

}
