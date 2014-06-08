package com.panamana.sharetaxi.model.utils;

import java.util.ArrayList;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * 
 * @author naama
 *
 */
public class LocationUtils {
	/**
	 * converts from Location to LatLng
	 * @author naama 
	 * @param location
	 * @return
	 */
	public static LatLng location2LatLng(Location location) {
		
		return	(location!=null) ? new LatLng(location.getLatitude(), location.getLongitude()):
				null;
	}		
	
	/**
	 * @author naama
	 * @param latlng
	 * @return
	 */
	public static String latlng2String(LatLng latLng) {
		return latLng.latitude+","+latLng.longitude;
	}

	/**
	 * @author naama
	 * @param latlng
	 * @return
	 */
	public static String[] latlng2String(LatLng... latLngs) {
		
		ArrayList<String> strings = new ArrayList<String>();
		for(LatLng latLng:latLngs) {
			strings.add(latLng.latitude+","+latLng.longitude);
		}
		return strings.toArray(new String[latLngs.length]);
	}

	
	/**
	 * converts from LatLng to Location
	 * @author naama
	 * @param latLng
	 * @return
	 */
	public static Location latLng2Location(LatLng latLng) {
		Location location = new Location("");
		if (latLng != null) {
			location.setLatitude(latLng.latitude);
			location.setLongitude(latLng.longitude);
		}
		return location;
	}


}
