package com.fenixedu.freeroomsfenix.api.models;

public class Campus extends Space {

	private Space containedSpaces[];

	public Campus() {
		super();
	}

	/**
	 * Instantiates a new campus.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 */
	public Campus(String id, String name, String type, Space spaces[]) {
		super(id, name, type);
		this.containedSpaces = spaces;
	}

	public Space[] getContainedSpaces() {
		return containedSpaces;
	}

	public void setContainedSpaces(Space[] spaces) {
		this.containedSpaces = spaces;
	}

}
