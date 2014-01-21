package com.fenixedu.freeroomsfenix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import pt.ist.fenixedu.android.FenixEduHttpResponseHandler;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Floor;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Room;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Room.RoomEvent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fenixedu.freeroomsfenix.helpers.FloorsHelper;
import com.fenixedu.freeroomsfenix.helpers.RoomsHelper;

public class ListFloorsAndRoomsFragment extends SherlockFragment {

	private FenixFreeRoomsApplication application;
	private Floor[] floors;

	private Map<String, List<FenixSpace.Room>> rooms;
	private Map<String, List<FenixSpace.Room>> filteredRooms;

	private ExpandableListView expandableListView;
	private FloorsAndRoomsExpandableListAdapter adapter;

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
		filteredRooms = rooms;

		loadFloorsList();
	}

	private void loadFloorsList() {
		FloorsHelper.loadFloors(application,
				new FenixEduHttpResponseHandler<Floor[]>(Floor[].class) {

					@Override
					public void onSuccess(Floor[] floorsArray) {
						updateFloorsList(floorsArray);
					}
				});
	}

	private void updateFloorsList(Floor[] floorsArray) {
		floors = floorsArray;
		adapter = new FloorsAndRoomsExpandableListAdapter();
		expandableListView.setAdapter(adapter);

		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (adapter.getChildrenCount(groupPosition) == 0) {
					loadFloorRooms(groupPosition);
					return true;
				}
				return false;
			}
		});
	}

	private void loadFloorRooms(final int groupPosition) {
		final Floor floor = floors[groupPosition];
		RoomsHelper.loadFloorRooms(floor, application,
				new FenixEduHttpResponseHandler<Room[]>(Room[].class) {

					@Override
					public void onSuccess(Room[] roomsArray) {
						updateFloorRooms(floor, roomsArray, groupPosition);
					}
				});
	}

	private void updateFloorRooms(Floor floor, Room[] roomsArray,
			int groupPosition) {
		List<Room> roomsList = new ArrayList<FenixSpace.Room>();
		for (Room room : roomsArray) {
			roomsList.add(room);
		}

		rooms.put(floor.getId(), roomsList);

		adapter.getFilter().filter("");

		expandableListView.expandGroup(groupPosition);

	}

	private class FloorsAndRoomsExpandableListAdapter extends
			BaseExpandableListAdapter implements Filterable {

		public Object getChild(int groupPosition, int childPosition) {
			Floor group = (Floor) getGroup(groupPosition);
			String floorId = group.getId();
			return filteredRooms.get(floorId).get(childPosition);
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
			Floor group = (Floor) getGroup(groupPosition);
			String groupId = group.getId();
			List<FenixSpace.Room> childrens = filteredRooms.get(groupId);

			if (childrens == null) {
				return 0;
			}
			return childrens.size();
		}

		public Object getGroup(int groupPosition) {
			return floors[groupPosition];
		}

		public int getGroupCount() {
			return floors.length;
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

		public Filter getFilter() {
			return new RoomsFilter();
		}

	}

	private class RoomsFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			Map<String, List<FenixSpace.Room>> filteredRooms = new HashMap<String, List<Room>>();

			for (FenixSpace floor : floors) {
				String floorId = floor.getId();
				List<FenixSpace.Room> floorRooms = rooms.get(floorId);
				List<FenixSpace.Room> floorFilteredRooms = new ArrayList<FenixSpace.Room>();

				if (floorRooms == null) {
					continue;
				}

				for (FenixSpace.Room r : floorRooms) {
					RoomEvent event = RoomsHelper.getNextEvent(r, application);
					// There's no events... So the room is free
					if (event == null) {
						floorFilteredRooms.add(r);
					} else {
						DateTime eventDateTime = RoomsHelper.getDateTime(event,
								application, event.getStart());
						DateTime searchDateTime = RoomsHelper.getDateTime(
								event, application,
								application.getSearchTimeAsString());
						if (eventDateTime.isAfter(searchDateTime)) {
							floorFilteredRooms.add(r);
						}

					}
				}
				filteredRooms.put(floorId, floorFilteredRooms);
			}

			results.values = filteredRooms;
			results.count = filteredRooms.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredRooms = (Map<String, List<Room>>) results.values;
			adapter.notifyDataSetChanged();

		}

	}

}
