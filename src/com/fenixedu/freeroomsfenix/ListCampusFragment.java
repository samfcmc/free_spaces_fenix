package com.fenixedu.freeroomsfenix;

import pt.ist.fenixedu.sdk.FenixEduAndroidClient;
import pt.ist.fenixedu.sdk.FenixEduAndroidClientFactory;
import pt.ist.fenixedu.sdk.FenixEduConfig;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.actionbarsherlock.app.SherlockFragment;

public class ListCampusFragment extends SherlockFragment {

	private ListView listView;

	private FenixSpace[] campus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_list_campus);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater
				.inflate(R.layout.activity_list_campus, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (ListView) getView().findViewById(
				R.id.listview_list_campus_campus);

		loadCampusList();
	}

	private void updateList() {
		listView.setAdapter(new CampusListAdapter(getSherlockActivity()));
	}

	private void loadCampusList() {
		new LoadCampusListAsyncTask().execute();
	}

	private class CampusListAdapter extends ArrayAdapter<FenixSpace> {

		public CampusListAdapter(Context context) {
			super(context, R.layout.activity_list_campus, campus);
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

			return convertView;
		}
	}

	private class LoadCampusListAsyncTask extends
			AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			String consumerKey = "7065221202521";
			String consumerSecret = "zHaiWfLxfW1Wt8rbwusDAx8lt24Z4PvG0tyzSS7607nxRZb23mTNBqVZzab9KGzU45RMH4z2tn8e4PJ7xDB8OSuTzBE0dBs8BN3vKatTb4rX1BNWcTq";
			String accessToken = "NzA3MzgxMTE0MzU1NDpkNTNmZjFmOTUwZGIxMjU3NGNkN2ZlNThlMmVhZmU3";
			String baseUrl = "https://fenix.ist.utl.pt";
			String callbackUrl = "http://localhost:8000/login";
			FenixEduConfig config = new FenixEduConfig(consumerKey,
					consumerSecret, accessToken, baseUrl, callbackUrl);
			FenixEduAndroidClient client = FenixEduAndroidClientFactory
					.getSingleton(config);
			campus = client.getSpaces();
			return Integer.valueOf(campus.length);
		}

		@Override
		protected void onPostExecute(Integer result) {
			updateList();
		}

	}

}
