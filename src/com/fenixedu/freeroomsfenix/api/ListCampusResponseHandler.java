package com.fenixedu.freeroomsfenix.api;

import com.fenixedu.freeroomsfenix.api.models.Campus;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class ListCampusResponseHandler extends
		AsyncHttpResponseHandler {

	Gson gson = new Gson();

	public ListCampusResponseHandler() {
		super();

	}

	@Override
	public void onSuccess(String response) {
		Campus[] instance = gson.fromJson(response, Campus[].class);
		onSuccess(instance);
	}

	public abstract void onSuccess(Campus[] object);

}
