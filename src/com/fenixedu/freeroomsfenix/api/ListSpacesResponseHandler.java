package com.fenixedu.freeroomsfenix.api;

import com.fenixedu.freeroomsfenix.api.models.Space;

public abstract class ListSpacesResponseHandler extends SpaceResponseHandler {

	public ListSpacesResponseHandler() {
		super();

	}

	@Override
	public void onSuccess(String response) {
		Space[] instance = getGson().fromJson(response, Space[].class);
		onSuccess(instance);
	}

	public abstract void onSuccess(Space[] object);

}
