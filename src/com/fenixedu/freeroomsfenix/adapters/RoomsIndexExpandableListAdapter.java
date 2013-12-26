package com.fenixedu.freeroomsfenix.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

/**
 * The Class RoomsIndexExpandableListAdapter.
 */
public class RoomsIndexExpandableListAdapter extends BaseExpandableListAdapter {

	/** The floors. */
	private Space[] floors;

	/**
	 * The extra floors. Nested floors. In some cases, when we send a request to
	 * get spaces inside a floor we get some kind of sub-floors
	 */
	private List<Space> extraFloors;

	/** The rooms. */
	private final Map<String, Space[]> rooms;

	/** The context. */
	private Context context;

	/**
	 * Instantiates a new rooms index expandable list adapter.
	 */
	public RoomsIndexExpandableListAdapter() {
		super();
		this.rooms = new HashMap<String, Space[]>();
		this.extraFloors = new ArrayList<Space>();
	}

	/**
	 * Instantiates a new rooms index expandable list adapter.
	 * 
	 * @param context
	 *            the context
	 */
	public RoomsIndexExpandableListAdapter(Context context) {
		super();
		this.rooms = new HashMap<String, Space[]>();
		this.context = context;
		this.extraFloors = new ArrayList<Space>();
	}

	/**
	 * Gets the floors.
	 * 
	 * @return the floors
	 */
	public Space[] getFloors() {
		return floors;
	}

	/**
	 * Sets the floors.
	 * 
	 * @param floors
	 *            the new floors
	 */
	public void setFloors(Space[] floors) {
		this.floors = floors;
	}

	/**
	 * Gets the rooms.
	 * 
	 * @return the rooms
	 */
	public Map<String, Space[]> getRooms() {
		return rooms;
	}

	public void addExtraFloor(Space floor) {
		this.extraFloors.add(floor);
	}

	/**
	 * Adds the rooms.
	 * 
	 * @param floor
	 *            the floor
	 */
	public void addRooms(Building floor) {
		List<Space> containedSpaces = new ArrayList<Space>();
		for (Space space : floor.getContainedSpaces()) {
			if (space.getType().equals("FLOOR")) {
				addExtraFloor(space);
			} else {
				containedSpaces.add(space);
			}
		}
		rooms.put(floor.getId(),
				containedSpaces.toArray(new Space[containedSpaces.size()]));
	}

	/**
	 * Instantiates a new rooms index expandable list adapter.
	 * 
	 * @param floors
	 *            the floors
	 * @param rooms
	 *            the rooms
	 * @param context
	 *            the context
	 */
	public RoomsIndexExpandableListAdapter(Space[] floors,
			Map<String, Space[]> rooms, Context context) {
		super();
		this.floors = floors;
		this.rooms = rooms;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	public Object getChild(int groupPosition, int childPosition) {
		Space group = (Space) getGroup(groupPosition);
		Space[] childs = this.rooms.get(group.getId());
		return childs[childPosition];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	public int getChildrenCount(int groupPosition) {
		Space group = (Space) getGroup(groupPosition);
		String groupID = group.getId();
		Space[] childs = rooms.get(groupID);
		if (childs == null) {
			return 0;
		} else {
			return childs.length;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	public Object getGroup(int groupPosition) {
		if (groupPosition < floors.length) {
			return floors[groupPosition];
		} else {
			int index = groupPosition - floors.length;
			return extraFloors.get(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	public int getGroupCount() {
		return floors.length + extraFloors.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	public boolean hasStableIds() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
