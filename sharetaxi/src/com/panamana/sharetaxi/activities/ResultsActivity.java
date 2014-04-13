package com.panamana.sharetaxi.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.panamana.sharetaxi.R;

public class ResultsActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_results);

		TabHost tabHost = getTabHost();

		String tab1 = "че 4";
		String tab2 = "че 4 а";
		String tab3 = "че 5";

		// Tab for KAV 4
		TabSpec timesSpec = tabHost.newTabSpec(tab1);
		timesSpec.setIndicator(tab1,
				getResources().getDrawable(R.drawable.icon_times_tab));
		Intent timesIntent = new Intent(this, Kav4Activity.class);
		timesSpec.setContent(timesIntent);

		// Tab for KAV 4A
		TabSpec stationsSpec = tabHost.newTabSpec(tab2);
		// setting Title and Icon for the Tab
		stationsSpec.setIndicator(tab2,
				getResources().getDrawable(R.drawable.icon_stations_tab));
		Intent stationsIntent = new Intent(this, Kav4_A_Activity.class);
		stationsSpec.setContent(stationsIntent);

		// Tab for KAV 5
		TabSpec mapSpec = tabHost.newTabSpec(tab3);
		mapSpec.setIndicator(tab3,
				getResources().getDrawable(R.drawable.icon_map_tab));
		Intent mapIntent = new Intent(this, Kav5Activity.class);
		mapSpec.setContent(mapIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(timesSpec); // Adding times tab
		tabHost.addTab(stationsSpec); // Adding stations tab
		tabHost.addTab(mapSpec); // Adding map tab

		// FREE CODE

		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

	}
}