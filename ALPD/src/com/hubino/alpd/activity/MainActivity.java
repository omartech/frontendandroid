package com.hubino.alpd.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
* The Class MainActivity.
* 
* @author Hubino
* @version 1.0.0 - The Class MainActivity Created
* 
*/

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
