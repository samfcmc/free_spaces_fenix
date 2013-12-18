package com.fenixedu.freeroomsfenix.api;

import com.fenixedu.freeroomsfenix.api.models.Building;

public abstract class BuildingResponseHandler extends SpaceResponseHandler {

	public BuildingResponseHandler() {
		super();
	}

	@Override
	public void onSuccess(String response) {
		Building instance = getGson().fromJson(response, Building.class);
		onSuccess(instance);
	}

	public abstract void onSuccess(Building building);

}
