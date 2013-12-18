package com.fenixedu.freeroomsfenix.api;

import com.fenixedu.freeroomsfenix.api.models.Space;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class ListSpacesResponseHandler extends
		AsyncHttpResponseHandler {

	Gson gson = new Gson();

	public ListSpacesResponseHandler() {
		super();

	}

	@Override
	public void onSuccess(String response) {
		Space[] instance = gson.fromJson(response, Space[].class);
		onSuccess(instance);
	}

	public abstract void onSuccess(Space[] object);

}
