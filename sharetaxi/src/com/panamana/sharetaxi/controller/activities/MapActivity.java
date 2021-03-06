package com.panamana.sharetaxi.controller.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.addressSearch.tasks.DownloadTask;
import com.panamana.sharetaxi.cars.CarsWorker;
import com.panamana.sharetaxi.cars.locations.updater.LocationsUpdateThread;
import com.panamana.sharetaxi.cars.objects.Car;
import com.panamana.sharetaxi.controller.dialogs.DialogAbout;
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.lines.objects.LineDirectionPair;
import com.panamana.sharetaxi.lines.workers.LinesWorker;
import com.panamana.sharetaxi.model.maps.MapManager;
import com.panamana.sharetaxi.model.utils.Marker_Arrow;

/**
 * Main Activity.
 * 
 * @author naama
 */
public class MapActivity extends ActionBarActivity implements OnMarkerClickListener{
	
	// constants:
	private static final String TAG = MapActivity.class.getSimpleName();
	private static final boolean DEBUG = false;
	
	// fields:
	public static Context context;
	LocationsUpdateThread updater;
	public MapManager mapManager;
	public static Map<LineDirectionPair,Boolean> linesToHide = new HashMap<LineDirectionPair, Boolean>();


	// Life Cycle //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		if(DEBUG) Log.i(TAG, "onCreate");
			
		initLinesMap();
		updateLinesToHide();
		
		context = this;
		// create map
		mapManager = new MapManager(this);
		// set map position
		mapManager.positionMap(LINES.LINE4_WAYPOINTS.getStart());
		if(DEBUG) Log.i(TAG, "draw line");
		MapManager.map.setOnMarkerClickListener((OnMarkerClickListener)this);

		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      if(DEBUG) Log.i(TAG, query);
	      doMySearch(query);
	      
	    }
	    
	}


	private void doMySearch(String query) {
      String url = "https://maps.googleapis.com/maps/api/geocode/json?";
      
      try {
          // encoding special characters like space in the user input place
          query = URLEncoder.encode(query, "utf-8");
          
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
      String address = "address=" + query;

      String sensor = "sensor=false";

      // url , from where the geocoding data is fetched
      url = url + address + "&" + sensor;

      // Instantiating DownloadTask to get places from Google Geocoding service
      // in a non-ui thread
      DownloadTask downloadTask = new DownloadTask(context,mapManager);

      // Start downloading the geocoding places
      downloadTask.execute(url);
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(DEBUG) Log.i(TAG, "onResume");
		// Check if update needed
		if(DEBUG) Log.i(TAG,String.valueOf(SettingsActivity.settingVisited));
		if (SettingsActivity.settingVisited) {
			if(DEBUG) Log.i(TAG,String.valueOf(SettingsActivity.settingVisited));
			if (DEBUG) Log.i(TAG,linesToHide.toString());
			updateLinesToHide();
			MapManager.removeCars();
			if (DEBUG) Log.i(TAG,linesToHide.toString());
			SettingsActivity.settingVisited = false;
		}
		if(DEBUG) Log.i(TAG,String.valueOf(SettingsActivity.settingVisited));
		if(DEBUG) Log.i(TAG,"hidePolylines will start now");
		mapManager.HidePolylines(linesToHide);
		// draw route
		if (MapManager.polylineOptionsMap == null
				|| MapManager.polylineOptionsMap.isEmpty()) {
			// no line data - request data from server
			LinesWorker lw = new LinesWorker(this, mapManager, LINES.LINE4_WAYPOINTS, LINES.LINE4A_WAYPOINTS, LINES.LINE5_WAYPOINTS) {
				@Override
				public void onLineWorkerComplete() {
					if(!isFinishing() && mapManager!=null) {
						// NOT onDestroy AND got MapManager
						for(final String line : MapManager.polylineOptionsMap.keySet()){
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
		if(DEBUG) Log.i(TAG, "onPause");
		if (updater != null) {
			updater.pause();
		}
		MapManager.removeCars();
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

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		
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
		Toast.makeText(getApplicationContext(), getString(R.string.mapToastline), Toast.LENGTH_LONG).show();
	}

	private void clickOpenInfo() {
		startActivity(new Intent(this, StaticDataActivity.class));
		Toast.makeText(getApplicationContext(), getString(R.string.infoToastline), Toast.LENGTH_LONG).show();

	}

	private void clickOpenSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
		Toast.makeText(getApplicationContext(), getString(R.string.settingToastline), Toast.LENGTH_LONG).show();

	}
	@ Override
	public boolean onMarkerClick(Marker marker) {
	
		try {
			Object [] markersArray = MapManager.markersMap.values().toArray();
			int i = 0;
			while (i<CarsWorker.cars.size() && i<markersArray.length) {
					// valid index
					if(marker.equals(((Marker_Arrow) (markersArray[i])).getMarker())) {
						// found the marker
						String carID = (String)MapManager.markersMap.keySet().toArray()[i];
						Car carById = CarsWorker.cars.get(carID);
						carById.updateEstimatedTime();
						break;
					// marker not found yet
				}
					
					i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void updateLinesToHide() {
		
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
