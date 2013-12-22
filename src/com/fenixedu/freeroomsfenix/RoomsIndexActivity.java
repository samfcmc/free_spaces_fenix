package com.fenixedu.freeroomsfenix;

import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.fenixedu.freeroomsfenix.adapters.RoomsIndexExpandableListAdapter;
import com.fenixedu.freeroomsfenix.api.BuildingResponseHandler;
import com.fenixedu.freeroomsfenix.api.FenixEduAPI;
import com.fenixedu.freeroomsfenix.api.models.Building;
import com.fenixedu.freeroomsfenix.api.models.Space;

public class RoomsIndexActivity extends Activity {

	private String currentBuildingID;

	private final FenixEduAPI api = FenixEduAPI.getInstance();

	private ExpandableListView expandableListView;
	RoomsIndexExpandableListAdapter listAdapter;

	Map<String, Space[]> rooms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooms_index);
		// Show the Up button in the action bar.
		setupActionBar();

		init();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rooms_index, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {
		Intent intent = getIntent();
		currentBuildingID = intent.getExtras().getString("buildingID");

		expandableListView = (ExpandableListView) findViewById(R.id.rooms_rooms_index_expandable_list);

		listAdapter = new RoomsIndexExpandableListAdapter(this);

		loadFloors();
	}

	private void loadFloors() {
		api.getSpace(currentBuildingID, new BuildingResponseHandler() {

			@Override
			public void onSuccess(Building building) {
				listAdapter.setFloors(building.getContainedSpaces());
				expandableListView.setAdapter(listAdapter);
				initExpandableListViewBehaviour();
			}
		});
	}

	private void initExpandableListViewBehaviour() {
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (listAdapter.getChildrenCount(groupPosition) == 0) {
					Space group = (Space) listAdapter.getGroup(groupPosition);
					loadSpacesInside(group.getId(), parent, v, groupPosition,
							id);
					return true;
				} else {
					// Data is already in the list... Just load the child items
					return false;
				}

			}
		});
	}

	private void loadSpacesInside(final String spaceID,
			final ExpandableListView listView, final View view,
			final int groupPosition, final long id) {
		final ProgressDialog dialog = ProgressDialog.show(
				RoomsIndexActivity.this, "", "Loading...", true);

		api.getSpace(spaceID, new BuildingResponseHandler() {

			@Override
			public void onSuccess(Building building) {
				addRooms(building);
				dialog.dismiss();
				listView.performItemClick(view, groupPosition, id);
			}
		});
	}

	private void addRooms(Building buildingRooms) {
		listAdapter.addRooms(buildingRooms);
		expandableListView.setAdapter(listAdapter);

	}

}
