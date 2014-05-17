package com.panamana.sharetaxi.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.panamana.sharetaxi.R;

public class DialogAbout extends Dialog
implements
OnClickListener {

	private static final String TAG = DialogAbout.class.getSimpleName();
	private static final boolean DEBUG = false;

	public DialogAbout(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_about);
		((Button) findViewById(R.id.dialog_about_ok)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(DEBUG) {
			Log.i(TAG, "onClick");
		}
		switch (v.getId()) {
		case R.id.dialog_about_ok:
			dismiss();
			break;
		default:
			break;
		}
	}
}
