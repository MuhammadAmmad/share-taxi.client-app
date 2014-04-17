package com.panamana.sharetaxi.directions;

import com.google.android.gms.maps.model.LatLng;

public class Lines {

	public static Line line4 = new Line(
			new LatLng(32.0538589,34.780081),
			new LatLng(32.0985456, 34.7802936),
			new LatLng[] {
				new LatLng(32.0549137,34.7754676),
				new LatLng(32.0600602,34.7783),
				new LatLng(32.0605694,34.7739441),
				new LatLng(32.0728617, 34.7683222),
				new LatLng(32.0958599, 34.7760255) }
			);
	
	public static Line line4a = new Line(
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
}
