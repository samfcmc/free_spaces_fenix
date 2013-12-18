package com.fenixedu.freeroomsfenix.api;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class SpaceResponseHandler extends AsyncHttpResponseHandler {

	private final Gson gson = new Gson();

	public SpaceResponseHandler() {
	}

	@Override
	public abstract void onSuccess(String response);

	public Gson getGson() {
		return gson;
	}
}
