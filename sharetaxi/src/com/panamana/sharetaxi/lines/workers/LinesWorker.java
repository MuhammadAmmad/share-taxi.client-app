package com.panamana.sharetaxi.lines.workers;

import android.content.Context;

import com.panamana.sharetaxi.lines.objects.Line;
import com.panamana.sharetaxi.maps.MapManager;

/**
 * 
 * @author naama
 */
public abstract class LinesWorker extends Thread {

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
		// 2. finish, got lines data from server
		onLineWorkerComplete();		
	}
	
	public abstract void onLineWorkerComplete();
	
}
