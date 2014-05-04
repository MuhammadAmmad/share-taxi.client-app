package com.panamana.sharetaxi.maps;

import com.panamana.sharetaxi.directions.Line;

import android.app.Activity;
import android.content.Context;

public abstract class LinesWorker extends Thread {

	Context context;
	Line[] lines;
	
	public LinesWorker(Context context, Line... lines) {
		this.context = context;
		this.lines = lines;
	}
	
	@Override
	public void run() {
		super.run();
		
		// 1. creates polylines
		for(Line l : lines) {
			// start new thread
			LineWorker lw = new LineWorker(l);
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
