package com.panamana.sharetaxi.controller.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.panamana.sharetaxi.R;

public class DialogAbout extends Dialog implements OnClickListener {
	Activity mActivity;
	Button okButton, facebookButton, shareButton, contactButton;

	String share_msg = "היי מה שלומך? אני רוצה לשתף איתך תוכנה מומלצת למעקב ותזמון קוי מוניות שירות מספר 4 5 באזור תא, הורד בחינם מהקישור הבא",
			thanks_for_share = "תודה על שיתוף האפליקציה",
			email_first_line = "צוות sharetaxi שלום רב",
			thanks_for_email = "אנו מודים לך אל האימייל נחזור אליך בהקדם האפשרי",
			email = "sharetaxi@googlegroups.com";

	
	
	
	String facebookWebPage = "http://www.facebook.com";
	String Url_for_download = "https://www.dropbox.com/s/3uuxe1czqj3jmsq/ShareTaxi.apk";

	
	
	
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
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent
					.putExtra("sms_body", share_msg + "\n" + Url_for_download);
			sendIntent.setType("vnd.android-dir/mms-sms");
			mActivity.startActivity(sendIntent);

			// Toast.makeText(getApplicationContext(), thanks_for_share,
			// Toast.LENGTH_LONG).show();
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
