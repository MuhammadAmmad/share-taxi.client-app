package com.panamana.sharetaxi.activities;


import com.panamana.sharetaxi.R;

import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class aboutActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.fragment_about);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		
		//fix the random update name to static them

		final LayoutInflater factory = getLayoutInflater();

		final View textEntryView = factory.inflate(R.layout.fragment_about, null);
		TextView develName1=(TextView) textEntryView.findViewById(R.id.name1_TextView);
		TextView develName2=(TextView) textEntryView.findViewById(R.id.name2_TextView);
		TextView develName3=(TextView) textEntryView.findViewById(R.id.name3_TextView);
		TextView develName4=(TextView) textEntryView.findViewById(R.id.name4_TextView);
		TextView develName5=(TextView) textEntryView.findViewById(R.id.name5_TextView);
		TextView develName6=(TextView) textEntryView.findViewById(R.id.name6_TextView);
		
		develName1.setText(getString(R.string.devel_name1));
		develName2.setText(getString(R.string.devel_name2));
		develName3.setText(getString(R.string.devel_name3));
		develName4.setText(getString(R.string.devel_name4));
		develName5.setText(getString(R.string.devel_name5));
		develName6.setText(getString(R.string.devel_name6));
		//
		
		
	}

	public void sendEmail(View view) {
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						
						//We can get To,CC,BCC,Subject,Body information from user by using EditText
						emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
						emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"fassaf.f@gmail.com"});
						//emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{"lmn@xyz.com"});
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
						emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_first_line));
						
						emailIntent.setType("message/rfc822");
						startActivity(emailIntent);
						
						Toast.makeText(getApplicationContext(), getString(R.string.thanks_for_email), Toast.LENGTH_LONG)
						.show();
						
		
	}

	
	public void goToFacebookWeb(View view){
		Intent getFacebookWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebookWebPage)));			             
		startActivity(getFacebookWebPage);		
	}
	
	
	public void shareAPP(View view) {	
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body",  getString(R.string.share_msg)+"\n"+ getString(R.string.Url_for_download)); 
		sendIntent.setType("vnd.android-dir/mms-sms");
		startActivity(sendIntent);
		
		Toast.makeText(getApplicationContext(), getString(R.string.thanks_for_share), Toast.LENGTH_LONG)
		.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about,
					container, false);
			return rootView;
		}
	}

}

