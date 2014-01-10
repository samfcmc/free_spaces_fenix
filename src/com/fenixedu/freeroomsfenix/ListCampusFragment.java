package com.fenixedu.freeroomsfenix;

import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

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
		new LoadCampusListAsyncTask().execute();
	}

	private void onClickCampus(int position) {
		adapter.setSelectedPosition(position);
		adapter.notifyDataSetChanged();
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		Tab currentTab = actionBar.getSelectedTab();
		Tab nextTab = actionBar.getTabAt(currentTab.getPosition() + 1);
		application.setCurrentSelectedCampus(position);
		actionBar.selectTab(nextTab);
	}

	private class CampusListAdapter extends ArrayAdapter<FenixSpace> {

		private int selectedPosition;;

		public CampusListAdapter(Context context) {
			super(context, R.layout.fragment_list_campus, campus);
			this.selectedPosition = application.getCurrentSelectedCampus();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater(getArguments());
				convertView = inflater.inflate(R.layout.campus_list_item,
						parent, false);
			}
			FenixSpace space = getItem(position);
			RadioButton radioButton = (RadioButton) convertView
					.findViewById(R.id.radiobutton_campus_list_item_campus);
			radioButton.setText(space.getName());

			if (selectedPosition == position) {
				radioButton.setChecked(true);
			} else {
				radioButton.setChecked(false);
			}

			return convertView;
		}

		public void setSelectedPosition(int position) {
			this.selectedPosition = position;
		}

	}

	private class LoadCampusListAsyncTask extends
			AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			campus = application.getFenixEduClient().getSpaces();
			return Integer.valueOf(campus.length);
		}

		@Override
		protected void onPostExecute(Integer result) {
			updateList();
		}

	}

}
