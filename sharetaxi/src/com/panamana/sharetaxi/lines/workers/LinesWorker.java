package com.panamana.sharetaxi.lines.workers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.lines.objects.Line;
import com.panamana.sharetaxi.model.maps.MapManager;

/**
 * 
 * @author naama
 */
public abstract class LinesWorker extends Thread {

	private static final double LINE_OFFSET = 0.0003;
	
	// Fields:
	private Context context;
	private Line[] lines;
	private  MapManager maps;
	
	// Constructor:
	public LinesWorker(Context context, MapManager maps, Line... lines) {
		this.context = context;
		this.lines = lines;
		this.maps = maps;
	}
	
	// Methods:
	
	@Override
	public void run() {
		super.run();
		
		// 1. creates polylines
		for(Line l : lines) {
			// start new thread
			LineWorker lw = new LineWorker(l,maps);
			lw.start();
			try {
				// wait thread finish
				lw.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (MapManager.polylineOptionsMap.get(LINES.LINE4) != null && MapManager.polylineOptionsMap.get(LINES.LINE4a) != null) {
			List<LatLng> line4 = MapManager.polylineOptionsMap.get(LINES.LINE4).getPoints();
			List<LatLng> line4a = MapManager.polylineOptionsMap.get(LINES.LINE4a).getPoints();
			line4 = shiftPolylineOptions	(line4,	LINE_OFFSET);
			line4a = shiftPolylineOptions	(line4a,LINE_OFFSET);
			PolylineOptions line4aPolyline = new PolylineOptions();
			line4aPolyline.addAll(line4).addAll(line4a)
			.color(LINES.LINE4A_WAYPOINTS.getColor())
			.width(LINES.LINE4A_WAYPOINTS.getWidth());
			MapManager.polylineOptionsMap.put(LINES.LINE4a, line4aPolyline);
		}
		// 2. finish, got lines data from server
		onLineWorkerComplete();		
	}
	
	private List<LatLng> shiftPolylineOptions(List<LatLng> points,double offset) {
		List<LatLng> shiftedPoints = new ArrayList<LatLng>();
		for (LatLng point: points) {
			shiftedPoints.add(new LatLng(point.latitude, point.longitude+offset));
		}
		return shiftedPoints;
	}

	public abstract void onLineWorkerComplete();
	
}
