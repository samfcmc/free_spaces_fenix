package com.fenixedu.freeroomsfenix.api.models;

public class Campus extends Space {

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
	public Campus(String id, String name, String type) {
		super(id, name, type);
	}

}
