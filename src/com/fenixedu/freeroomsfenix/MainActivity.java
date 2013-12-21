package com.fenixedu.freeroomsfenix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

	private Button searchButton;

	private String currentBuildingID;

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

	public String getCurrentBuildingID() {
		return currentBuildingID;
	}

	public void setCurrentBuildingID(String currentBuildingID) {
		this.currentBuildingID = currentBuildingID;
	}

	private void init() {
		campusSpinner = (Spinner) findViewById(R.id.campus_main_spinner);
		buildingsSpinner = (Spinner) findViewById(R.id.buildings_main_spinner);
		searchButton = (Button) findViewById(R.id.search_main_button);

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

		initBuildingsSpinnerBehaviour();

		initSearchButtonBehaviour();
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

	private void initBuildingsSpinnerBehaviour() {
		buildingsSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> adapterView,
							View view, int position, long id) {
						if (buildingsSpinnerAdapter.getCount() > 0) {
							Space space = buildingsSpinnerAdapter
									.getItem(position);
							setCurrentBuildingID(space.getId());
						}
					}

					public void onNothingSelected(AdapterView<?> adapterView) {
					}
				});
	}

	private void initSearchButtonBehaviour() {
		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goToRoomsIndex();
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
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Loading...", true);
		api.getSpace(spaceID, new CampusResponseHandler() {

			@Override
			public void onSuccess(Campus object) {
				fillSpinnerAdapter(buildingsSpinnerAdapter,
						object.getContainedSpaces());
				dialog.dismiss();
			}
		});
	}

	private void goToRoomsIndex() {
		if (currentBuildingID == null) {
			// TODO: Handle this in a different way
			return;
		}
		Intent intent = new Intent(this, RoomsIndexActivity.class);
		intent.putExtra("buildingID", currentBuildingID);
		startActivity(intent);
	}
}
