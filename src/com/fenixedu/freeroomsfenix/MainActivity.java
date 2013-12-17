package com.fenixedu.freeroomsfenix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fenixedu.freeroomsfenix.api.FenixEduAPI;
import com.fenixedu.freeroomsfenix.api.ListCampusResponseHandler;
import com.fenixedu.freeroomsfenix.api.models.Campus;

public class MainActivity extends Activity {

	private final FenixEduAPI api = FenixEduAPI.getInstance();

	private Spinner campusSpinner;
	private ArrayAdapter<Campus> campusSpinnerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		loadSpaces();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void init() {
		campusSpinner = (Spinner) findViewById(R.id.campus_main_spinner);
	}

	private void loadCampusSpinner(Campus[] campusArray) {
		campusSpinnerAdapter = new ArrayAdapter<Campus>(this,
				android.R.layout.simple_spinner_item, campusArray);
		campusSpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		campusSpinner.setAdapter(campusSpinnerAdapter);
	}

	private void loadSpaces() {
		api.getSpaces(new ListCampusResponseHandler() {

			@Override
			public void onSuccess(Campus[] object) {
				loadCampusSpinner(object);
			}

			@Override
			public void onFailure(Throwable e) {
				// text.setText(e.getMessage());
			}
		});
	}
}
