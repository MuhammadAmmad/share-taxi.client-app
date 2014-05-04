package com.panamana.sharetaxi.threads;

import com.panamana.sharetaxi.maps.Maps;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @author naama
 *
 */
public class LocationsUpdateThread extends Thread {
	
	
	private static final String TAG = LocationsUpdateThread.class.getSimpleName();
	private static final long INTERVAL = 20000;
	private boolean isRunning = true;
	private Context context;
	Maps maps;
	
	
	
	public LocationsUpdateThread(Context context, Maps maps) {
		super();
		this.context = context;
		this.maps=maps;
	}

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
