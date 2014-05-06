package com.panamana.sharetaxi.cars.tasks;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.cars.CarsWorker;
import com.panamana.sharetaxi.cars.objects.Car;
import com.panamana.sharetaxi.maps.MapManager;
import com.panamana.sharetaxi.utils.Position;

public class CarsDirectionTask {
	
	public void updateCarDirection() {
		for(Car car : CarsWorker.cars.values() ) {
			LatLng carLatLngLocation = car.getLatLng();
			String lineName = car.getLineName();
			PolylineOptions linePolylineOptions = MapManager.polylineOptionsMap.get(lineName);
			List<LatLng> linePoints = linePolylineOptions.getPoints(); 
			for (int i = 0; i<linePoints.size(); i++) {
				Position xyzPoint = LatLng2XYZ(linePoints.get(i));
			}
		}
		
	}

	private Position LatLng2XYZ(LatLng latLng) {
		// TODO Auto-generated method stub
		return null;
	}

}
