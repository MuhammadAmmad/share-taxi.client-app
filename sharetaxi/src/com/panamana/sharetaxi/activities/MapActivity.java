package com.panamana.sharetaxi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.directions.Lines;
import com.panamana.sharetaxi.maps.LinesWorker;
import com.panamana.sharetaxi.maps.Maps;
import com.panamana.sharetaxi.threads.LocationsUpdateThread;

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
	public Maps maps;

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
		maps = new Maps(this);
		// set map position
		maps.positionMap(Lines.line4.getStart());
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
		if (maps.polylineOptionsMap == null
				|| maps.polylineOptionsMap.isEmpty()) {
			// no line data - request data from server
			LinesWorker lw = new LinesWorker(this, maps, Lines.line4, Lines.line4a, Lines.line5) {
				@Override
				public void onFinish() {
					for(final String line : maps.polylineOptionsMap.keySet()){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								maps.addPolyline(line);
							}
						});
					}
				}
			};
			lw.start();

		} else {
			// got line data - add/draw
			for(String line : maps.polylineOptionsMap.keySet()){
				maps.addPolyline(line);
			}
		}
		updater = new LocationsUpdateThread(this,maps);
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
		maps.removeCars();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// save data
		// new Persist().save(this, FILENAME, Maps.polylineOptionsMap);
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
		maps.getPolyline("line4").setVisible(false);
	}

	private void clickOpenInfo() {
		startActivity(new Intent(this, ResultsActivity.class));
	}

	private void clickOpenSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}

}
