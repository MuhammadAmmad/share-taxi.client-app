package com.panamana.sharetaxi.controller.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.cars.locations.updater.LocationsUpdateThread;
import com.panamana.sharetaxi.controller.dialogs.DialogAbout;
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.lines.objects.Line;
import com.panamana.sharetaxi.lines.objects.LineDirectionPair;
import com.panamana.sharetaxi.lines.workers.LinesWorker;
import com.panamana.sharetaxi.model.maps.MapManager;

/**
 * Main Activity.
 * 
 * @author
 */
public class MapActivity extends ActionBarActivity {

	//
	// Lines map
	private static final String TAG = MapActivity.class.getSimpleName();
	private static final String FILENAME = "polylines.data";
	private static final boolean DEBUG = true;
	public static Context context;
	LocationsUpdateThread updater;
	public MapManager mapManager;
//	public static String [] polylinesToHide = {};
	public static Map<LineDirectionPair,Boolean> linesToHide = new HashMap<LineDirectionPair, Boolean>();


	// Life Cycle //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initLinesMap();
		updateLinesToHide();
		if(DEBUG) {
			Log.i(TAG, "onCreate");
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		if(DEBUG) {
			Log.i(TAG, "onCreate");
		}
		//
		context = this;
		// create map
		mapManager = new MapManager(this);
		// set map position
		mapManager.positionMap(LINES.LINE4_WAYPOINTS.getStart());
		if(DEBUG) {
			Log.i(TAG, "draw line");
		}

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
		if(DEBUG) {
			Log.i(TAG, "onResume");
		}
		// Check if update needed
		updateLinesToHide();
		if (SettingsActivity.settingVisited) {
			SettingsActivity.settingVisited = false;
		}
		mapManager.HidePolylines(linesToHide);
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
			for(String line : MapManager.polylineOptionsMap.keySet()){
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
		if(DEBUG) {
			Log.i(TAG, "onPause");
		}
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
			
		case R.id.action_about:
			clickAbout();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void clickAbout() {
		new DialogAbout(this).show();
	}

	private void clickOpenMap() {
	}

	private void clickOpenInfo() {
		startActivity(new Intent(this, StaticDataActivity.class));
	}

	private void clickOpenSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	private void updateLinesToHide() {
		
		List<String> tempCars = new ArrayList<String>();
		List<String> tempLines = new ArrayList<String>();
		
		if (!SettingsActivity.b1_isChecked) {
//			tempCars.add(LINES.LINE4_N);
			linesToHide.put(LineDirectionPair.LINE4NORTH, false);
		}
		else {
			linesToHide.put(LineDirectionPair.LINE4NORTH, true);
		}
		if (!SettingsActivity.b2_isChecked) {
			linesToHide.put(LineDirectionPair.LINE4SOUTH, false);		
		}
		else {
			linesToHide.put(LineDirectionPair.LINE4SOUTH, true);		
		}
		
		if (!SettingsActivity.b3_isChecked) {
//			tempCars.add(LINES.LINE4_N);
			linesToHide.put(LineDirectionPair.LINE4ANORTH, false);
		}
		else {
			linesToHide.put(LineDirectionPair.LINE4ANORTH, true);
		}
		if (!SettingsActivity.b4_isChecked) {
			linesToHide.put(LineDirectionPair.LINE4ASOUTH, false);		
		}
		else {
			linesToHide.put(LineDirectionPair.LINE4ASOUTH, true);		
		}

		
		
		if (!SettingsActivity.b5_isChecked) {
//			tempCars.add(LINES.LINE4_N);
			linesToHide.put(LineDirectionPair.LINE5NORTH, false);
		}
		else {
			linesToHide.put(LineDirectionPair.LINE5NORTH, true);
		}
		if (!SettingsActivity.b6_isChecked) {
			linesToHide.put(LineDirectionPair.LINE5SOUTH, false);		
		}
		else {
			linesToHide.put(LineDirectionPair.LINE5SOUTH, true);		
		}
		
//		linesToHide.put(LineDirectionPair.LINE4NORTH, false);
//		linesToHide.put(LineDirectionPair.LINE4SOUTH, false);
	}
	
	public static void initLinesMap() {
		LINES.linesMap.put(LINES.LINE4, LINES.LINE4A_WAYPOINTS);
		LINES.linesMap.put(LINES.LINE4a, LINES.LINE4A_WAYPOINTS);
		LINES.linesMap.put(LINES.LINE5, LINES.LINE5_WAYPOINTS);
	}

}
