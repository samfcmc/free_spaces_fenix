package com.fenixedu.freeroomsfenix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.fenixedu.android.FenixEduHttpResponseHandler;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Building;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Floor;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Room;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fenixedu.freeroomsfenix.helpers.RoomsHelper;

public class ListFloorsAndRoomsFragment extends SherlockFragment {

	private FenixFreeRoomsApplication application;
	private List<FenixSpace> floors;

	private Map<String, List<FenixSpace.Room>> rooms;

	private ExpandableListView expandableListView;
	private FloorsAndRoomsExpandableListAdapter adapter;

	private int roomsToLoad;

	public ListFloorsAndRoomsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_list_floors_rooms, container,
				false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			openSearch();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSearch() {
		SherlockDialogFragment fragment = new SearchDialogFragment();
		fragment.show(getFragmentManager(), "search");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		application = (FenixFreeRoomsApplication) getActivity()
				.getApplication();
		expandableListView = (ExpandableListView) getView().findViewById(
				R.id.expandablelistview_floors_rooms_list);

		TextView buildingNameTextView = (TextView) getView().findViewById(
				R.id.textview_floors_rooms_building_name);

		buildingNameTextView
				.setText(application.getCurrentBuilding().getName());

		rooms = new HashMap<String, List<FenixSpace.Room>>();

		loadFloorsList();
	}

	private void updateFloorsList(FenixSpace.Building building) {
		floors = building.getContainedSpaces();
		adapter = new FloorsAndRoomsExpandableListAdapter();
		expandableListView.setAdapter(adapter);

		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (adapter.getChildrenCount(groupPosition) == 0) {
					loadFloor(groupPosition);
					return true;
				}
				return false;
			}
		});
	}

	private void loadFloorsList() {
		String buildingId = application.getCurrentBuilding().getId();
		String day = application.getDateAsString();
		application.getFenixEduClient().getSpace(
				buildingId,
				day,
				new FenixEduHttpResponseHandler<FenixSpace.Building>(
						FenixSpace.Building.class) {

					@Override
					public void onSuccess(Building building) {
						updateFloorsList(building);
					}

				});
	}

	private void loadFloor(final int groupPosition) {
		FenixSpace floor = (FenixSpace) adapter.getGroup(groupPosition);
		String floorId = floor.getId();
		String day = application.getDateAsString();

		application.getFenixEduClient().getSpace(
				floorId,
				day,
				new FenixEduHttpResponseHandler<FenixSpace.Floor>(
						FenixSpace.Floor.class) {

					@Override
					public void onSuccess(Floor floor) {
						loadFloorRooms(floor, groupPosition);
					}
				});
	}

	private void loadFloorRooms(final FenixSpace.Floor floor,
			final int groupPosition) {
		// Update floor in list
		for (FenixSpace floorSpace : floors) {
			if (floorSpace.getId().equals(floor.getId())) {
				floors.set(groupPosition, floor);
			}
		}
		rooms.put(floor.getId(), new ArrayList<FenixSpace.Room>());
		roomsToLoad = floor.getContainedSpaces().size();

		loadRoom(floor, groupPosition);
	}

	private void loadRoom(final FenixSpace.Floor floor, final int groupPosition) {
		if (roomsToLoad == 0) {
			expandableListView.performItemClick(
					adapter.getGroupView(groupPosition, false, null, null),
					groupPosition, adapter.getGroupId(groupPosition));
		} else {
			roomsToLoad--;
			FenixSpace room = floor.getContainedSpaces().get(roomsToLoad);
			String spaceId = room.getId();
			String day = application.getDateAsString();
			application.getFenixEduClient().getSpace(
					spaceId,
					day,
					new FenixEduHttpResponseHandler<FenixSpace.Room>(
							FenixSpace.Room.class) {

						@Override
						public void onSuccess(Room room) {
							addRoom(floor.getId(), room, groupPosition);
							loadRoom(floor, groupPosition);
						}

					});
		}
	}

	private void addRoom(String floorId, FenixSpace.Room room,
			final int groupPosition) {
		rooms.get(floorId).add(room);
	}

	private class FloorsAndRoomsExpandableListAdapter extends
			BaseExpandableListAdapter {

		public Object getChild(int groupPosition, int childPosition) {
			String floorId = floors.get(groupPosition).getId();
			return rooms.get(floorId).get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			FenixSpace.Room child = (FenixSpace.Room) getChild(groupPosition,
					childPosition);

			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater(getArguments());
				convertView = inflater.inflate(R.layout.room_list_item, parent,
						false);
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.textview_room_list_item);
			TextView nextEventTextView = (TextView) convertView
					.findViewById(R.id.textview_room_list_next_event);
			FenixSpace.Room.RoomEvent event = RoomsHelper.getNextEvent(child,
					application);
			String eventString;
			if (event == null) {
				eventString = "NA";
			} else {
				eventString = event.getWeekday() + " " + event.getStart();
			}

			textView.setText(child.getName());
			nextEventTextView.setText(eventString);

			return convertView;
		}

		public int getChildrenCount(int groupPosition) {
			FenixSpace group = (FenixSpace) getGroup(groupPosition);
			String groupId = group.getId();
			List<FenixSpace.Room> childrens = rooms.get(groupId);

			if (childrens == null
					|| childrens.size() < group.getContainedSpaces().size()) {
				return 0;
			}
			return childrens.size();
		}

		public Object getGroup(int groupPosition) {
			return floors.get(groupPosition);
		}

		public int getGroupCount() {
			return floors.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			FenixSpace group = (FenixSpace) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater(getArguments());
				convertView = inflater.inflate(R.layout.floor_list_item,
						parent, false);
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.textview_floor_list_item);

			textView.setText(group.getName());

			return convertView;
		}

		public boolean hasStableIds() {
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}

}
