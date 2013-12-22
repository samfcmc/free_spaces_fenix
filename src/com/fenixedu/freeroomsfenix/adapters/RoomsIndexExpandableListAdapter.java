package com.fenixedu.freeroomsfenix.adapters;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fenixedu.freeroomsfenix.R;
import com.fenixedu.freeroomsfenix.api.models.Building;
import com.fenixedu.freeroomsfenix.api.models.Space;

public class RoomsIndexExpandableListAdapter extends BaseExpandableListAdapter {

	private Space[] floors;
	private final Map<String, Space[]> rooms;
	private Context context;

	public RoomsIndexExpandableListAdapter() {
		super();
		this.rooms = new HashMap<String, Space[]>();
	}

	public RoomsIndexExpandableListAdapter(Context context) {
		super();
		this.rooms = new HashMap<String, Space[]>();
		this.context = context;
	}

	public Space[] getFloors() {
		return floors;
	}

	public void setFloors(Space[] floors) {
		this.floors = floors;
	}

	public Map<String, Space[]> getRooms() {
		return rooms;
	}

	public void addRooms(Building floor) {
		rooms.put(floor.getId(), floor.getContainedSpaces());
	}

	public RoomsIndexExpandableListAdapter(Space[] floors,
			Map<String, Space[]> rooms, Context context) {
		super();
		this.floors = floors;
		this.rooms = rooms;
		this.context = context;
	}

	public Object getChild(int groupPosition, int childPosition) {
		Space group = this.floors[groupPosition];
		Space[] childs = this.rooms.get(group.getId());
		return childs[childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parentView) {
		final Space space = (Space) getChild(groupPosition, childPosition);
		final String childText = space.getName();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.rooms_index_list_item, null);
		}

		TextView childTextView = (TextView) convertView
				.findViewById(R.id.room_rooms_index_list_item_textview);
		childTextView.setText(childText);

		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		Space group = floors[groupPosition];
		String groupID = group.getId();
		Space[] childs = rooms.get(groupID);
		if (childs == null) {
			return 0;
		} else {
			return childs.length;
		}
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
			View convertView, ViewGroup parentView) {
		final Space group = (Space) getGroup(groupPosition);
		final String groupText = group.getName();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rooms_index_list_group,
					null);
		}

		TextView textView = (TextView) convertView
				.findViewById(R.id.floor_rooms_list_group_textview);
		textView.setText(groupText);

		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
