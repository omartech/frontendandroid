package com.hubino.alpd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
* The Class ResultActivity.
* 
* @author Hubino
* @version 1.0.0 - The Class ResultActivity Created
* 
*/

public class ResultActivity extends Activity implements OnClickListener {

	private Button home;
	public static String userId;
	private TextView textView;
	public static String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_activity);

		home = (Button) findViewById(R.id.home);
		
		textView = (TextView) findViewById(R.id.textView1);
		textView.setText(message);

		home.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
