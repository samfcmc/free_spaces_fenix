package com.fenixedu.freeroomsfenix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.fenixedu.android.FenixEduHttpResponseHandler;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Building;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace.Floor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ListFloorsAndRoomsFragment extends SherlockFragment {

	private FenixFreeRoomsApplication application;
	private List<FenixSpace> floors;

	private Map<String, List<FenixSpace>> rooms;

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

		rooms = new HashMap<String, List<FenixSpace>>();

		loadFloors();
	}

	private void updateFloorsList(FenixSpace.Building building) {
		floors = building.getContainedSpaces();
		adapter = new FloorsAndRoomsExpandableListAdapter();
		expandableListView.setAdapter(adapter);

		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (adapter.getChildrenCount(groupPosition) == 0) {
					loadRooms(groupPosition);
					return true;
				}
				return false;
			}
		});
	}

	private void loadFloors() {
		String buildingId = application.getCurrentBuilding().getId();
		String day = application.getTimeAsString();
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

	private void loadRooms(final int groupPosition) {
		FenixSpace floor = (FenixSpace) adapter.getGroup(groupPosition);
		String floorId = floor.getId();
		String day = application.getTimeAsString();

		application.getFenixEduClient().getSpace(
				floorId,
				day,
				new FenixEduHttpResponseHandler<FenixSpace.Floor>(
						FenixSpace.Floor.class) {

					@Override
					public void onSuccess(Floor floor) {
						addRooms(floor);
						expandableListView.performItemClick(
								adapter.getGroupView(groupPosition, false,
										null, null), groupPosition, adapter
										.getGroupId(groupPosition));
					}
				});
	}

	private void addRooms(FenixSpace.Floor floor) {
		rooms.put(floor.getId(), floor.getContainedSpaces());

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
			FenixSpace child = (FenixSpace) getChild(groupPosition,
					childPosition);

			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater(getArguments());
				convertView = inflater.inflate(R.layout.room_list_item, parent,
						false);
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.textview_room_list_item);

			textView.setText(child.getName());

			return convertView;
		}

		public int getChildrenCount(int groupPosition) {
			String groupId = floors.get(groupPosition).getId();
			List<FenixSpace> childrens = rooms.get(groupId);
			if (childrens == null) {
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
