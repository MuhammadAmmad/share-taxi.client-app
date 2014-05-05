package com.panamana.sharetaxi.lines;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.panamana.sharetaxi.lines.objects.Line;

/**
 * static data class for Line (and car) objects.
 * @author naama
 */
public class LINES {

	// Line names
	
	public static final String LINE4 = "line4";
	public static final String LINE4a = "line4a";
	public static final String LINE5 = "line5";
	public static final String PREFIX = "line";
	
	// Line way points
	
	public static Line LINE4_WAYPOINTS = new Line(
			LINES.LINE4,
			Color.BLUE,
			5,
			new LatLng(32.0538589,34.780081),
			new LatLng(32.0985456, 34.7802936),
			new LatLng[] {
				new LatLng(32.0549137,34.7754676),
				new LatLng(32.0600602,34.7783),
				new LatLng(32.0605694,34.7739441),
				new LatLng(32.0728617, 34.7683222),
				new LatLng(32.0958599, 34.7760255) }
			);
	
	public static Line LINE4A_WAYPOINTS = new Line(
			LINES.LINE4a,
			Color.GRAY,
			5,
			new LatLng(32.0985456, 34.7802936),
			new LatLng(32.1294424,34.7926878),
			new LatLng[] {
				new LatLng(32.0987302,34.7833047),
				new LatLng(32.1019062,34.7819771),
				new LatLng(32.1051123,34.790843),
				new LatLng(32.110365,34.7891264),
				new LatLng(32.1247177,34.802773),
				new LatLng(32.1249721,34.7980952),
				new LatLng(32.1223189,34.7974515)}
			);
	
	public static Line LINE5_WAYPOINTS = new Line(
			LINES.LINE5,
			Color.GREEN,
			5,
			new LatLng(32.0562598,34.7810158),
			new LatLng(32.0941057,34.798036),
			new LatLng[] {
				new LatLng(32.0590423,34.7779045),
				new LatLng(32.0618400,34.7777057),
				new LatLng(32.0624417,34.7744929),
				new LatLng(32.0720304,34.7792675),
				new LatLng(32.0741580,34.779006 ),
				new LatLng(32.0923583,34.7764054),
				new LatLng(32.0940017,34.7834732),
//				new LatLng(32.0937948,34.7904164),
				new LatLng(32.0965916,34.8040227)
				}
			);
}
