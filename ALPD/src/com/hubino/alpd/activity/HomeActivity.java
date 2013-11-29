package com.hubino.alpd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
* The Class HomeActivity.
* 
* @author Hubino
* @version 1.0.0 - The Class HomeActivity Created
* 
*/

public class HomeActivity extends Activity implements OnClickListener {

	private Button mPrepareMetaDataButton;
	private Button mValiadteButton;
	public static String userId;
	private EditText editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		mPrepareMetaDataButton = (Button) findViewById(R.id.preparemetadata);
		mValiadteButton = (Button) findViewById(R.id.validate);
		
		editText = (EditText) findViewById(R.id.editText1);

		mPrepareMetaDataButton.setOnClickListener(this);
		mValiadteButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.preparemetadata:
			CameraActivity.commCount = 5;
			userId = editText.getText().toString();
			Intent intent = new Intent(this, CameraActivity.class);
			startActivity(intent);
			finish();
			finish();
			break;
		case R.id.validate:
			CameraActivity.commCount = 3;
			userId = editText.getText().toString();
			Intent intent1 = new Intent(this, CameraActivity.class);
			startActivity(intent1);
			finish();
			finish();
			break;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/*@Override
	public void onBackPressed() {
	}*/

	@Override
	public void onStop() {
		super.onStop();
	}
}
