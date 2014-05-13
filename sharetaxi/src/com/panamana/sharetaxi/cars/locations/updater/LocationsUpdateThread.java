package com.panamana.sharetaxi.cars.locations.updater;

import com.panamana.sharetaxi.model.maps.MapManager;

import android.content.Context;
import android.util.Log;

/**
 * sample server for new taxi location data
 * @author naama
 */
public class LocationsUpdateThread extends Thread {
	
	// Constants:
	private static final String TAG = LocationsUpdateThread.class.getSimpleName();
	private static final long INTERVAL = 1000;

	// Fields:
	private boolean isRunning = true;
	private Context context;
	private MapManager maps;
	
	// Constructor:
	public LocationsUpdateThread(Context context, MapManager maps) {
		super();
		this.context = context;
		this.maps=maps;
	}

	// Methods:
	
	/**
	 * 
	 * @author naama
	 */
	public void run() {
		Log.i(TAG,"start thread");
		while(isRunning ){
			// do
			backgroundTask();
			// wait
			try {Thread.sleep(INTERVAL);} catch (InterruptedException e) {}
		}
		Log.i(TAG,"stop thread");
	}
	
	private void backgroundTask() {
		Log.i(TAG,"tik");
		maps.drawCars(context);
	}

	public void pause(){
		isRunning = false;
	}
}
