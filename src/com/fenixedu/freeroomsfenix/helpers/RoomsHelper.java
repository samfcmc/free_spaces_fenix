package com.fenixedu.freeroomsfenix.helpers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixedu.android.FenixEduHttpResponseHandler;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Floor;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Room;

import com.fenixedu.freeroomsfenix.FenixFreeRoomsApplication;

/**
 * The Class RoomsHelper.
 */
public class RoomsHelper {

	private static FenixEduHttpResponseHandler<Room[]> responseHandlerAfterGetAllRooms;
	private static int roomsToLoad = 0;
	private static FenixSpace[] roomsArray;
	private static Room[] rooms;

	/**
	 * Gets the next event.
	 * 
	 * @param room
	 *            the room
	 * @param application
	 *            the application
	 * @return the next event
	 */
	public static FenixSpace.Room.RoomEvent getNextEvent(FenixSpace.Room room,
			FenixFreeRoomsApplication application) {

		if (room.getEvents().size() > 0) {
			DateTime nextEventDateTime = null;
			FenixSpace.Room.RoomEvent nextEvent = null;

			for (FenixSpace.Room.RoomEvent event : room.getEvents()) {
				DateTime eventStartDateTime = getDateTime(event, application,
						event.getStart());

				if (eventStartDateTime.isAfterNow()) {
					if (nextEvent == null
							|| eventStartDateTime.isBefore(nextEventDateTime
									.getMillis())
							&& eventStartDateTime.isAfterNow()) {
						nextEventDateTime = eventStartDateTime;
						nextEvent = event;
					}
				}

			}
			return nextEvent;

		}
		return null;
	}

	/**
	 * Gets the day of week.
	 * 
	 * @param event
	 *            the event
	 * @return the day of week
	 */
	private static int getDayOfWeek(FenixSpace.Room.RoomEvent event) {
		String weekDay = event.getWeekday();

		if (weekDay.equals("Seg")) {
			return DateTimeConstants.MONDAY;
		} else if (weekDay.equals("Ter")) {
			return DateTimeConstants.TUESDAY;
		} else if (weekDay.equals("Qua")) {
			return DateTimeConstants.WEDNESDAY;
		} else if (weekDay.equals("Qui")) {
			return DateTimeConstants.THURSDAY;
		} else if (weekDay.equals("Sex")) {
			return DateTimeConstants.FRIDAY;
		} else if (weekDay.equals("Sáb")) {
			return DateTimeConstants.SATURDAY;
		} else {
			return DateTimeConstants.SUNDAY;
		}
	}

	/**
	 * Gets the date time.
	 * 
	 * @param event
	 *            the event
	 * @param application
	 *            the application
	 * @param hour
	 *            the hour in format HH:mm
	 * @return the date time
	 */
	public static DateTime getDateTime(FenixSpace.Room.RoomEvent event,
			FenixFreeRoomsApplication application, String hour) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(application
				.getDatePattern() + " e HH:mm");
		String timeString = application.getDateAsString() + " "
				+ getDayOfWeek(event) + " " + hour;
		DateTime result = formatter.parseDateTime(timeString);

		return result;
	}

	public static void loadFloorRooms(Floor floor,
			final FenixFreeRoomsApplication application,
			FenixEduHttpResponseHandler<Room[]> responseHandler) {
		responseHandlerAfterGetAllRooms = responseHandler;

		loadRoomsFromFloor(floor, application);

	}

	private static void loadRoomsFromFloor(Floor floor,
			FenixFreeRoomsApplication application) {
		roomsToLoad = floor.getContainedSpaces().size();
		roomsArray = floor.getContainedSpaces().toArray(
				new FenixSpace[roomsToLoad]);
		rooms = new Room[roomsToLoad];

		loadRoomRecursive(application);

	}

	private static void loadRoomRecursive(
			final FenixFreeRoomsApplication application) {
		if (roomsToLoad == 0) {
			responseHandlerAfterGetAllRooms.onSuccess(rooms);
		} else {
			roomsToLoad--;
			String roomId = roomsArray[roomsToLoad].getId();
			application.getFenixEduClient().getSpace(roomId,
					application.getDateAsString(),
					new FenixEduHttpResponseHandler<Room>(Room.class) {

						@Override
						public void onSuccess(Room room) {
							rooms[roomsToLoad] = room;
							loadRoomRecursive(application);
						}
					});
		}
	}

}
