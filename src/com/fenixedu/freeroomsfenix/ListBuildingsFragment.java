package com.fenixedu.freeroomsfenix;

import pt.ist.fenixedu.android.FenixEduHttpResponseHandler;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;

public class ListBuildingsFragment extends SherlockFragment {

	private FenixFreeRoomsApplication application;
	private FenixSpace.Campus campus;

	private ListView listView;
	private BuildingsListAdapter adapter;

	public ListBuildingsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_list_buildings, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		application = (FenixFreeRoomsApplication) getActivity()
				.getApplication();

		listView = (ListView) getView().findViewById(
				R.id.listview_buildings_list);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				onBuildingClick(position);
			}
		});

		loadCampus();
	}

	private void loadCampus() {
		String campusId = application.getCurrentCampus().getId();
		String day = application.getDateAsString();
		application.getFenixEduClient().getSpace(
				campusId,
				day,
				new FenixEduHttpResponseHandler<FenixSpace.Campus>(
						FenixSpace.Campus.class) {

					@Override
					public void onSuccess(FenixSpace.Campus space) {
						campus = space;
						updateBuildingsList();
					}
				});
	}

	private void updateBuildingsList() {
		adapter = new BuildingsListAdapter(getSherlockActivity());
		listView.setAdapter(adapter);
	}

	private void onBuildingClick(int position) {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		Tab currentTab = actionBar.getSelectedTab();
		Tab nextTab = actionBar.getTabAt(currentTab.getPosition() + 1);
		application.setCurrentSelectedBuildingPosition(position);
		application.setCurrentBuilding(adapter.getItem(position));
		actionBar.selectTab(nextTab);
	}

	private class BuildingsListAdapter extends ArrayAdapter<FenixSpace> {

		public BuildingsListAdapter(Context context) {
			super(context, R.layout.fragment_list_buildings, campus
					.getContainedSpaces());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FenixSpace building = getItem(position);
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater(getArguments());
				convertView = inflater.inflate(R.layout.building_list_item,
						parent, false);
			}

			CheckedTextView checkedTextView = (CheckedTextView) convertView
					.findViewById(R.id.checktextview_building_name);
			checkedTextView.setText(building.getName());

			return convertView;
		}
	}

}
