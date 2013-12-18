package com.fenixedu.freeroomsfenix.api;

import com.fenixedu.freeroomsfenix.api.models.Campus;

public abstract class CampusResponseHandler extends SpaceResponseHandler {

	public CampusResponseHandler() {
		super();
	}

	@Override
	public void onSuccess(String response) {
		Campus instance = getGson().fromJson(response, Campus.class);
		onSuccess(instance);
	}

	public abstract void onSuccess(Campus object);

}
