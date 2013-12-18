package com.fenixedu.freeroomsfenix.api;

import com.loopj.android.http.AsyncHttpClient;

/**
 * The Class FenixEduAPI.
 */
public class FenixEduAPI {

	/** The instance. */
	private static FenixEduAPI instance;

	/** The client. */
	private static AsyncHttpClient client = new AsyncHttpClient();

	/** The base url. */
	private static String baseURL = "https://fenix.ist.utl.pt/api/fenix/v1/";

	/*
	 * Endpoints
	 */
	/** The spaces endpoint. */
	private static String spacesEndpoint = "spaces";

	/*
	 * URLS
	 */
	private static String spacesURL = baseURL + spacesEndpoint + "/";

	/**
	 * Instantiates a new fenix edu api.
	 */
	private FenixEduAPI() {
	}

	/**
	 * The get instance.
	 * 
	 * @return single instance of FenixEduAPI
	 */
	public static FenixEduAPI getInstance() {
		if (instance == null) {
			instance = new FenixEduAPI();
		}
		return instance;
	}

	/**
	 * Gets the spaces.
	 * 
	 * @return the spaces
	 */
	public void getSpaces(ListSpacesResponseHandler responseHandler) {
		client.get(spacesURL, responseHandler);
	}

	public void getSpace(String spaceID, CampusResponseHandler responseHandler) {
		String url = spacesURL + spaceID;
		client.get(url, responseHandler);
	}

}
