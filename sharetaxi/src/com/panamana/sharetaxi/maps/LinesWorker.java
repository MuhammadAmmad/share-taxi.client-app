package com.panamana.sharetaxi.maps;

import android.content.Context;

import com.panamana.sharetaxi.directions.Line;

public abstract class LinesWorker extends Thread {

	Context context;
	Line[] lines;
	Maps maps;
	
	public LinesWorker(Context context, Maps maps, Line... lines) {
		this.context = context;
		this.lines = lines;
		this.maps = maps;
	}
	
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
		// 2. finish
		onFinish();		
	}
	
	public abstract void onFinish();
	
}
