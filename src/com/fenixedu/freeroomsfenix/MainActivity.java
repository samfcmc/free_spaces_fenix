package com.fenixedu.freeroomsfenix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {

	private Button buttonExplore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		buttonExplore = (Button) findViewById(R.id.button_main_explore);

		buttonExplore.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goToSpacesActivity();
			}
		});
	}

	private void goToSpacesActivity() {
		Intent intent = new Intent(this, SpacesActivity.class);
		startActivity(intent);
	}

}
