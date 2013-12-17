package com.fenixedu.freeroomsfenix.api.models;

/**
 * The Class Space. Used to map json object space return by the api to a java
 * class
 */
public abstract class Space {

	/** The id. */
	private String id;

	/** The name. */
	private String name;

	/** The type. */
	private String type;

	/**
	 * Instantiates a new space.
	 */
	public Space() {
	}

	/**
	 * Instantiates a new space.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 */
	public Space(String id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
