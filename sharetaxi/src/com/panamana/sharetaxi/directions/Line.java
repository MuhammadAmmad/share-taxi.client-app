package com.panamana.sharetaxi.directions;

import com.google.android.gms.maps.model.LatLng;

public class Line {

	String mName;
	int mColor;
	float mWidth;
	LatLng mStart;
	LatLng mEnd;
	LatLng [] mWaypoints;

	public Line(String name,int color,float width, LatLng start, LatLng end, LatLng[] waypoints) {
		this.mName = name;
		this.mColor = color;
		this.mWidth = width;
		this.mStart = start;
		this.mEnd = end;
		this.mWaypoints = waypoints;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
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

	public float getWidth() {
		return mWidth;
	}

	public int getColor() {
		return mColor;
	}	
}
