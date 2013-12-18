package com.fenixedu.freeroomsfenix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fenixedu.freeroomsfenix.api.CampusResponseHandler;
import com.fenixedu.freeroomsfenix.api.FenixEduAPI;
import com.fenixedu.freeroomsfenix.api.ListSpacesResponseHandler;
import com.fenixedu.freeroomsfenix.api.models.Campus;
import com.fenixedu.freeroomsfenix.api.models.Space;

public class MainActivity extends Activity {

	private final FenixEduAPI api = FenixEduAPI.getInstance();

	private Spinner campusSpinner;
	private ArrayAdapter<Space> campusSpinnerAdapter;
	private Spinner buildingsSpinner;
	private ArrayAdapter<Space> buildingsSpinnerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		loadSpaces();
		initCampusSpinnerBehaviour();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void init() {
		campusSpinner = (Spinner) findViewById(R.id.campus_main_spinner);
		buildingsSpinner = (Spinner) findViewById(R.id.buildings_main_spinner);

		campusSpinnerAdapter = new ArrayAdapter<Space>(this,
				android.R.layout.simple_spinner_item);
		campusSpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		campusSpinner.setAdapter(campusSpinnerAdapter);

		buildingsSpinnerAdapter = new ArrayAdapter<Space>(this,
				android.R.layout.simple_spinner_item);
		buildingsSpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingsSpinner.setAdapter(buildingsSpinnerAdapter);

	}

	private void initCampusSpinnerBehaviour() {
		campusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				Space space = campusSpinnerAdapter.getItem(position);
				loadBuildings(space.getId());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void fillSpinnerAdapter(ArrayAdapter<Space> adapter,
			Space[] spacesArray) {
		adapter.clear();
		for (Space space : spacesArray) {
			adapter.add(space);
		}
	}

	private void loadSpaces() {
		api.getSpaces(new ListSpacesResponseHandler() {

			@Override
			public void onSuccess(Space[] object) {
				initCampusSpinnerBehaviour();
				fillSpinnerAdapter(campusSpinnerAdapter, object);
			}
		});
	}

	private void loadBuildings(String spaceID) {
		api.getSpace(spaceID, new CampusResponseHandler() {

			@Override
			public void onSuccess(Campus object) {
				fillSpinnerAdapter(buildingsSpinnerAdapter,
						object.getContainedSpaces());
			}
		});
	}
}
