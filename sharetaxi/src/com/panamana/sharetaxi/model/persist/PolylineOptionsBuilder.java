package com.panamana.sharetaxi.model.persist;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolylineOptionsBuilder {

	private int color;
	private float width;
	private float zindex;
	private List<LatLng> points;

	public PolylineOptionsBuilder(int color, float width, float zindex,
			List<LatLng> points) {
		super();
		this.color = color;
		this.width = width;
		this.zindex = zindex;
		this.points = points;
	}
	
	public PolylineOptions build() {
		PolylineOptions polylineOptions = new PolylineOptions();
		// set all polyline options values.
		polylineOptions.color(color);
		polylineOptions.width(width);
		polylineOptions.zIndex(zindex);
		polylineOptions.addAll(points);
		return polylineOptions;
	}
}
