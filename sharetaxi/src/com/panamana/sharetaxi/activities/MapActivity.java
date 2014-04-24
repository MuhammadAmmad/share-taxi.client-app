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
import com.panamana.sharetaxi.maps.Maps;

/**
 * Main Activity.
 * @author 
 */
public class MapActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);	
		//
		context=this;
		// create map
		Maps.createGoogleMap(this);
		// set map position
		Maps.positionMap(Lines.line4.getStart());		
		Log.i(TAG ,"draw line");
		// draw route
		Maps.drawLine(Lines.line4,context);
		Maps.drawLine(Lines.line4a,context);
		Maps.drawLine(Lines.line5,context);
		Maps.drawCars(context);
	}
	
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
	    	openMap();
	    	return true;
	    case R.id.action_info:
	    	openInfo();
	    	return true;
		case R.id.action_settings:
		    openSettings();
		    return true;
	    /*
	     * COMMENTED OUT BY Yahav.
	     * CAN BE DELETED
        case R.id.map_menu_item0:
            item0Clicked();

	    case R.id.map_menu_item1:
	            item1Clicked();
	            return true;
	        case R.id.map_menu_item2:
	            item2Clicked();
	            return true;
	        case R.id.map_menu_item3:
	            item3Clicked();
	            return true;
	        case R.id.map_menu_item4:
	            item4Clicked();
	            return true;
*/
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openMap() {
		/* We're already on the Map activity, so do nothing. 
		 No need to re-launch activity:
		 startActivity(new Intent(this, MapActivity.class));
		 to avoid flashing screen. */
	}
	
	private void openInfo() {
		startActivity(new Intent(this,ResultsActivity.class));
	}
	
	private void openSettings() {
		startActivity(new Intent(this,Kav5Activity.class));
	}


	
}

