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

public class ListCampusFragment extends SherlockFragment {

	private ListView listView;
	private CampusListAdapter adapter;

	private FenixSpace[] campus;

	private FenixFreeRoomsApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater
				.inflate(R.layout.fragment_list_campus, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		application = (FenixFreeRoomsApplication) getActivity()
				.getApplication();

		listView = (ListView) getView().findViewById(
				R.id.listview_list_campus_campus);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				onClickCampus(position);
			}
		});

		loadCampusList();

	}

	private void updateList() {
		adapter = new CampusListAdapter(getSherlockActivity());
		listView.setAdapter(adapter);
	}

	private void loadCampusList() {
		application.getFenixEduClient().getSpaces(
				new FenixEduHttpResponseHandler<FenixSpace[]>(
						FenixSpace[].class) {

					@Override
					public void onSuccess(FenixSpace[] spaces) {
						campus = spaces;
						updateList();
					}
				});
	}

	private void onClickCampus(int position) {
		adapter.setSelectedPosition(position);
		adapter.notifyDataSetChanged();
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		Tab currentTab = actionBar.getSelectedTab();
		Tab nextTab = actionBar.getTabAt(currentTab.getPosition() + 1);
		application.setCurrentSelectedCampusPosition(position);
		application.setCurrentCampus(adapter.getItem(position));
		actionBar.selectTab(nextTab);
	}

	private class CampusListAdapter extends ArrayAdapter<FenixSpace> {

		private int selectedPosition;;

		public CampusListAdapter(Context context) {
			super(context, R.layout.fragment_list_campus, campus);
			this.selectedPosition = application
					.getCurrentSelectedCampusPosition();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater(getArguments());
				convertView = inflater.inflate(R.layout.campus_list_item,
						parent, false);
			}
			FenixSpace space = getItem(position);
			CheckedTextView checkedTextView = (CheckedTextView) convertView
					.findViewById(R.id.checkedtextview_campus_list_item);
			checkedTextView.setText(space.getName());

			if (selectedPosition == position) {
				checkedTextView.setChecked(true);
			} else {
				checkedTextView.setChecked(false);
			}

			return convertView;
		}

		public void setSelectedPosition(int position) {
			this.selectedPosition = position;
		}

	}

}
