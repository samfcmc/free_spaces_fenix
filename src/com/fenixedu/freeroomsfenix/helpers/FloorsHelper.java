package com.fenixedu.freeroomsfenix.helpers;

import pt.ist.fenixedu.android.FenixEduHttpResponseHandler;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Building;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Floor;

import com.fenixedu.freeroomsfenix.FenixFreeRoomsApplication;

public class FloorsHelper {

	private static int floorsToLoad = 0;
	private static Floor[] floors;
	private static FenixSpace[] floorsArray;
	private static FenixEduHttpResponseHandler<Floor[]> responseHandlerAfterGetAllFloors;

	public static void loadFloors(final FenixFreeRoomsApplication application,
			FenixEduHttpResponseHandler<Floor[]> responseHandler) {
		String day = application.getDateAsString();
		String buildingId = application.getCurrentBuilding().getId();
		responseHandlerAfterGetAllFloors = responseHandler;

		application.getFenixEduClient().getSpace(buildingId, day,
				new FenixEduHttpResponseHandler<Building>(Building.class) {

					@Override
					public void onSuccess(Building building) {
						loadFloorsFromBuilding(building, application);

					}
				});
	}

	private static void loadFloorsFromBuilding(Building building,
			FenixFreeRoomsApplication application) {
		floorsToLoad = building.getContainedSpaces().size();
		floorsArray = building.getContainedSpaces().toArray(
				new FenixSpace[floorsToLoad]);
		floors = new Floor[floorsToLoad];
		loadFloorRecursive(application);

	}

	private static void loadFloorRecursive(
			final FenixFreeRoomsApplication application) {
		if (floorsToLoad == 0) {
			responseHandlerAfterGetAllFloors.onSuccess(floors);
		} else {
			floorsToLoad--;
			String floorId = floorsArray[floorsToLoad].getId();
			application.getFenixEduClient().getSpace(floorId,
					application.getDateAsString(),
					new FenixEduHttpResponseHandler<Floor>(Floor.class) {

						@Override
						public void onSuccess(Floor floor) {
							floors[floorsToLoad] = floor;
							loadFloorRecursive(application);

						}
					});
		}
	}

}
