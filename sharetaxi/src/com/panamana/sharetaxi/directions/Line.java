package com.panamana.sharetaxi.directions;

import com.google.android.gms.maps.model.LatLng;

public class Line {

	LatLng mStart;
	LatLng mEnd;
	LatLng [] mWaypoints;

	public Line(LatLng mStart, LatLng mEnd, LatLng[] mWaypoints) {
		this.mStart = mStart;
		this.mEnd = mEnd;
		this.mWaypoints = mWaypoints;
	}

	public LatLng getStart() {
		return mStart;
	}

	public LatLng getEnd() {
		return mEnd;
	}

	public LatLng[] getWaypoints() {
		return mWaypoints;
	}
	
}
