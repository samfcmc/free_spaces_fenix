package com.fenixedu.freeroomsfenix.api;

import com.fenixedu.freeroomsfenix.api.models.Campus;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class CampusResponseHandler extends AsyncHttpResponseHandler {

	private final Gson gson = new Gson();

	public CampusResponseHandler() {
		super();
	}

	@Override
	public void onSuccess(String response) {
		Campus instance = gson.fromJson(response, Campus.class);
		onSuccess(instance);
	}

	public abstract void onSuccess(Campus object);

}
