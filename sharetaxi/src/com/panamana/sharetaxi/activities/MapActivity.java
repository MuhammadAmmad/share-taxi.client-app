package com.panamana.sharetaxi.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.directions.Directions;
import com.panamana.sharetaxi.directions.Lines;
import com.panamana.sharetaxi.maps.Maps;

/**
 * Main Activity.
 * @author 
 */
public class MapActivity extends ActionBarActivity {

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);	
		// create map
		Maps.createGoogleMap(this);
		// set map position
		Maps.positionMap();		
		// draw route
		Directions.drawRoute(Lines.line4);
	}
	
	/*
	 * create the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_menu, menu);
	    return true;
	}
	
	/**
	 * 
	 * @param lineOptions
	 */
	public static void drawPolyline(PolylineOptions lineOptions) {
		lineOptions.color(Color.GREEN);
		lineOptions.width(5);
		Maps.drawPolyline(lineOptions);// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
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

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void item0Clicked() {
		startActivity(new Intent(this, MainActivity.class));
	}

	private void item1Clicked() {
		startActivity(new Intent(this, Kav4Activity.class));
	}

	private void item2Clicked() {
		startActivity(new Intent(this, Kav4_A_Activity.class));
	}

	private void item3Clicked() {
		startActivity(new Intent(this,Kav5Activity.class));
	}

	private void item4Clicked() {
		startActivity(new Intent(this, ResultsActivity.class));
	}

	
}

