package com.panamana.sharetaxi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.model.Marker;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.cars.locations.updater.LocationsUpdateThread;
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.lines.workers.LinesWorker;
import com.panamana.sharetaxi.maps.MapManager;

/**
 * Main Activity.
 * 
 * @author
 */
public class MapActivity extends ActionBarActivity {

	//
	private static final String TAG = MapActivity.class.getSimpleName();
	private static final String FILENAME = "polylines.data";
	public static Context context;
	LocationsUpdateThread updater;
	public MapManager mapManager;
	public static final String [] linesToHide = {LINES.LINE4_N,LINES.LINE4a_N};


	// Life Cycle //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		Log.i(TAG, "onCreate");
		//
		context = this;
		// create map
		mapManager = new MapManager(this);
		// set map position
		mapManager.positionMap(LINES.LINE4_WAYPOINTS.getStart());
		Log.i(TAG, "draw line");

		// Maps.drawLine(Lines.line4,context);
		// Maps.drawLine(Lines.line4a,context);
		// Maps.drawLine(Lines.line5,context);
		// Maps.drawCars(context);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// restore data
		// Maps.polylineOptionsMap = (new Persist()).restore(this, FILENAME);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		// draw route
		if (mapManager.polylineOptionsMap == null
				|| mapManager.polylineOptionsMap.isEmpty()) {
			// no line data - request data from server
			LinesWorker lw = new LinesWorker(this, mapManager, LINES.LINE4_WAYPOINTS, LINES.LINE4A_WAYPOINTS, LINES.LINE5_WAYPOINTS) {
				@Override
				public void onLineWorkerComplete() {
					if(!isFinishing() && mapManager!=null) {
						// NOT onDestroy AND got MapManager
						for(final String line : mapManager.polylineOptionsMap.keySet()){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if(!isFinishing() && mapManager!=null) {
										// NOT onDestroy AND got MapManager
										mapManager.addPolyline(line,linesToHide);
									}
								}
							});
						}
					}
				}
			};
			lw.start();

		} else {
			// got line data - add/draw
			for(String line : mapManager.polylineOptionsMap.keySet()){
				mapManager.addPolyline(line,linesToHide);
			}
		}
		updater = new LocationsUpdateThread(this,mapManager);
		updater.start();
		// Maps.removeCars();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		if (updater != null) {
			updater.pause();
		}
		mapManager.removeCars();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// save data
		// new Persist().save(this, FILENAME, Maps.polylineOptionsMap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapManager = null;
	}
	// Menu //

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_map:
			clickOpenMap();
			return true;
		case R.id.action_info:
			clickOpenInfo();
			return true;
		case R.id.action_settings:
			clickOpenSettings();
			return true;
			/*
			 * COMMENTED OUT BY Yahav. CAN BE DELETED case R.id.map_menu_item0:
			 * item0Clicked();
			 * 
			 * case R.id.map_menu_item1: item1Clicked(); return true; case
			 * R.id.map_menu_item2: item2Clicked(); return true; case
			 * R.id.map_menu_item3: item3Clicked(); return true; case
			 * R.id.map_menu_item4: item4Clicked(); return true;
			 */
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void clickOpenMap() {
	}

	private void clickOpenInfo() {
		startActivity(new Intent(this, ResultsActivity.class));
	}

	private void clickOpenSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}

}
