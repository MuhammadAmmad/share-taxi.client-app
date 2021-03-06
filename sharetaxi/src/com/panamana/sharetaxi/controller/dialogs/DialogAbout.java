package com.panamana.sharetaxi.controller.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.panamana.sharetaxi.R;

public class DialogAbout extends Dialog implements OnClickListener {
	Activity mActivity;
	Button okButton, facebookButton, shareButton, contactButton;

	String share_msg = "Hi, I want to share with you recommended app that helps you see the shared taxis nearby.\ndownload free: ",
			thanks_for_share = "���� �� ����� ���������",
			email_first_line = "���� sharetaxi ���� ��",
			thanks_for_email = "��� ����� �� �� ������� ����� ���� ����� ������",
			email = "sharetaxi@googlegroups.com";

	String facebookWebPage = "http://www.facebook.com/ShareTaxiApp";
	
	String Url_for_download = "https://play.google.com/store/apps/details?id=com.panamana.sharetaxi";

	
	
	
	public DialogAbout(Activity activity) {
		super(activity);
		mActivity = activity;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_about);
		okButton = (Button) findViewById(R.id.dialog_about_ok);
		okButton.setOnClickListener(this);

		facebookButton = (Button) findViewById(R.id.facebookButton);
		facebookButton.setOnClickListener(this);

		shareButton = (Button) findViewById(R.id.send_App_Button);
		shareButton.setOnClickListener(this);

		contactButton = (Button) findViewById(R.id.contact_as_button);
		contactButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {

		if (v == okButton) {
			dismiss();
		} else if (v == facebookButton) {
			Intent getFacebookWebPage = new Intent(Intent.ACTION_VIEW,
					Uri.parse(facebookWebPage));
			mActivity.startActivity(getFacebookWebPage);
		}

		else if (v == shareButton) {
			//send SMS
			/*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent
					.putExtra("sms_body", share_msg + "\n" + Url_for_download);
			sendIntent.setType("vnd.android-dir/mms-sms");
			mActivity.startActivity(sendIntent);
*/
			// Toast.makeText(getApplicationContext(), thanks_for_share,
			// Toast.LENGTH_LONG).show();
			
			
			//share
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, share_msg + "\n" + Url_for_download);
			sendIntent.setType("text/plain");
			mActivity.startActivity(sendIntent);
			
			
		}

		else if (v == contactButton) {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			emailIntent.putExtra(Intent.EXTRA_CC,
					new String[] { "fassaf.f@gmail.com" });
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			emailIntent.putExtra(Intent.EXTRA_TEXT, email_first_line);

			emailIntent.setType("message/rfc822");
			mActivity.startActivity(emailIntent);
			// Toast.makeText(getApplicationContext(), thanks_for_email),
			// Toast.LENGTH_LONG).show();
		}
	}
}
