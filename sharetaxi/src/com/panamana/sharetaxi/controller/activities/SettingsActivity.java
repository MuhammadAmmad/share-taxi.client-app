package com.panamana.sharetaxi.controller.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.panamana.sharetaxi.R;

public class SettingsActivity extends ActionBarActivity {
	
	public static boolean settingVisited = false;
	public static CheckBox b1 = null;
	public static CheckBox b2 = null;
	public static CheckBox b3 = null;
	public static CheckBox b4 = null;
	public static CheckBox b5 = null;
	public static CheckBox b6 = null;
	
	public static boolean b1_isChecked = true;
	public static boolean b2_isChecked = true;
	public static boolean b3_isChecked = true;
	public static boolean b4_isChecked = true;
	public static boolean b5_isChecked = true;
	public static boolean b6_isChecked = true;
	
	
//	public static boolean[] booleans = new boolean[] {true,true,true,true,true,true};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		final LayoutInflater factory = getLayoutInflater();
		final View checkBoxView = factory.inflate(R.layout.fragment_settings, null);
		
		b1 = (CheckBox)checkBoxView.findViewById(R.id.checkBoxLineKav4North);
		b2 = (CheckBox)checkBoxView.findViewById(R.id.checkBoxLineKav4South);
		b3 = (CheckBox)checkBoxView.findViewById(R.id.checkBoxLineKav4aNorth);
		b4 = (CheckBox)checkBoxView.findViewById(R.id.checkBoxLineKav4aSouth);
		b5 = (CheckBox)checkBoxView.findViewById(R.id.checkBoxLineKav5North);
		b6 = (CheckBox)checkBoxView.findViewById(R.id.checkBoxLineKav5South);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
/* Merge
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
 * 
 */
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	@Override
    protected void onResume() {
      super.onResume();
      // update checkboxes values
      b1.setChecked(booleans[0]);
      b2.setChecked(booleans[1]);
      b3.setChecked(booleans[2]);
      b4.setChecked(booleans[3]);
      b5.setChecked(booleans[4]);
      b6.setChecked(booleans[5]);
    }
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		super.onSaveInstanceState(savedInstanceState);
		
		boolean[] checkBoxesValues = {b1.isChecked(),b2.isChecked(),b3.isChecked(),
										b4.isChecked(),b5.isChecked(),b6.isChecked()};
		//save checkboxes values
		savedInstanceState.putBooleanArray("checkBoxesVals", checkBoxesValues);
	}
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		// restore checkboxes values
		booleans = savedInstanceState.getBooleanArray("checkBoxesVals");
        super.onRestoreInstanceState(savedInstanceState);
    }*/
	
	@Override
	protected void onPause() {
		super.onPause();
		
	/*	booleans[0] = b1.isChecked();
		booleans[1] = b2.isChecked();
		booleans[2] = b3.isChecked();
		booleans[3] = b4.isChecked();
		booleans[4] = b5.isChecked();
		booleans[5] = b6.isChecked();*/
		
		settingVisited = true;
	}
				
	public void checkBoxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    ((CheckBox) view).setChecked(checked);
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	    case R.id.checkBoxLineKav4North:
	 
	    	b1_isChecked = checked;
	    	break;
	    case R.id.checkBoxLineKav4South:
	    	b2_isChecked = checked;
	    	break;
	    case R.id.checkBoxLineKav4aNorth:
	    	b3_isChecked = checked;
	    	break;
	    case R.id.checkBoxLineKav4aSouth:
	    	b4_isChecked = checked;
	    	break;
	    case R.id.checkBoxLineKav5North:
	    	b5_isChecked = checked;
	    	break;
	    case R.id.checkBoxLineKav5South:
	    	b6_isChecked = checked;
	    	break;
	    default:
	    	break;
	    
	    }
	 
	    

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);
			
			/* Update the checkboxes  */
			b1 = (CheckBox)rootView.findViewById(R.id.checkBoxLineKav4North);
			b2 = (CheckBox)rootView.findViewById(R.id.checkBoxLineKav4South);
			b3 = (CheckBox)rootView.findViewById(R.id.checkBoxLineKav4aNorth);
			b4 = (CheckBox)rootView.findViewById(R.id.checkBoxLineKav4aSouth);
			b5 = (CheckBox)rootView.findViewById(R.id.checkBoxLineKav5North);
			b6 = (CheckBox)rootView.findViewById(R.id.checkBoxLineKav5South);
			
			b1.setChecked(b1_isChecked);
			b2.setChecked(b2_isChecked);
			b3.setChecked(b3_isChecked);
			b4.setChecked(b4_isChecked);
			b5.setChecked(b5_isChecked);
			b6.setChecked(b6_isChecked);
			
			return rootView;
		}
	}

}
