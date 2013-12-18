package com.fenixedu.freeroomsfenix.api.models;

public class Building extends Campus {
	private Space parentSpace;

	public Building() {
		super();
	}

	public Building(String id, String name, String type, Space[] spaces,
			Space parentSpace) {
		super(id, name, type, spaces);
		this.parentSpace = parentSpace;
	}

	public Space getParentSpace() {
		return parentSpace;
	}

	public void setParentSpace(Space parentSpace) {
		this.parentSpace = parentSpace;
	}

}
