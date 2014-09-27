package com.panamana.sharetaxi.model.utils;

import com.google.android.gms.maps.model.Marker;

/**
 * the taxi marker and arrow pair
 * @author naama
 *
 */
public class Marker_Arrow {

	public Marker marker;
	public Marker arrow;
	
	public Marker_Arrow(Marker marker, Marker arrow) {
		this.marker = marker;
		this.arrow = arrow;
	}

	public void remove() {
		if (this.marker != null && this.arrow != null) {
			marker.remove();
			arrow.remove();
		}
	}
	
	public Marker getMarker() {
		return marker;
	}

}
