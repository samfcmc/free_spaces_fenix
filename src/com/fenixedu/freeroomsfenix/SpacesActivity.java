package com.fenixedu.freeroomsfenix;

import android.content.res.Resources;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SpacesActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_spaces);
		initActionBarTabs();
	}

	private void initActionBarTabs() {
		// setup action bar for tabs
		Resources resources = getResources();
		String campusTabTitle = resources.getString(R.string.tab_spaces_campus);
		String buildingsTabTitle = resources
				.getString(R.string.tab_spaces_buildings);
		String roomsTabTitle = resources.getString(R.string.tab_spaces_rooms);
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
						new TabListenerImpl<ListCampusFragment>(this,
								"buildings", ListCampusFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(roomsTabTitle)
				.setTabListener(
						new TabListenerImpl<ListCampusFragment>(this, "rooms",
								ListCampusFragment.class));
		actionBar.addTab(tab);
	}
}
