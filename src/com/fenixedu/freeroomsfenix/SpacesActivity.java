package com.fenixedu.freeroomsfenix;

import android.content.res.Resources;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class SpacesActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_spaces);
		initActionBarTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.spaces, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			// Handled in fragment
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initActionBarTabs() {
		// setup action bar for tabs
		Resources resources = getResources();
		String campusTabTitle = resources.getString(R.string.tab_spaces_campus);
		String buildingsTabTitle = resources
				.getString(R.string.tab_spaces_buildings);
		String floorsAndRoomsTabTitle = resources
				.getString(R.string.tab_spaces_rooms);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		Tab tab = actionBar
				.newTab()
				.setText(campusTabTitle)
				.setTabListener(
						new TabListenerImpl<ListCampusFragment>(this, "campus",
								ListCampusFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(buildingsTabTitle)
				.setTabListener(
						new TabListenerImpl<ListBuildingsFragment>(this,
								"buildings", ListBuildingsFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(floorsAndRoomsTabTitle)
				.setTabListener(
						new TabListenerImpl<ListFloorsAndRoomsFragment>(this,
								"rooms", ListFloorsAndRoomsFragment.class));

		actionBar.addTab(tab);
	}
}
