package com.fenixedu.freeroomsfenix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.fenixedu.freeroomsfenix.api.FenixEduAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity {

	private final FenixEduAPI api = FenixEduAPI.getInstance();
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		loadSpaces();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initViews() {
		text = (TextView) findViewById(R.id.text_main);
	}

	private void loadSpaces() {
		api.getSpaces(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				text.setText(response);
			}

			@Override
			public void onFailure(Throwable exception) {
				text.setText(exception.getMessage());
			}
		});

	}

}
