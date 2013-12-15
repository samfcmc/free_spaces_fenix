package com.fenixedu.freeroomsfenix.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

// TODO: Auto-generated Javadoc
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
	public void getSpaces(AsyncHttpResponseHandler responseHandler) {
		client.get(spacesURL, responseHandler);
	}

}
