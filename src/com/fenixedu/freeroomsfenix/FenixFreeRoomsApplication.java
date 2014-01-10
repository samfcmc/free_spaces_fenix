package com.fenixedu.freeroomsfenix;

import pt.ist.fenixedu.sdk.FenixEduAndroidClient;
import pt.ist.fenixedu.sdk.FenixEduAndroidClientFactory;
import pt.ist.fenixedu.sdk.FenixEduConfig;
import android.app.Application;
import android.content.res.Resources;

public class FenixFreeRoomsApplication extends Application {

	FenixEduAndroidClient fenixEduClient;
	private int currentSelectedCampus;

	public FenixFreeRoomsApplication() {
		currentSelectedCampus = -1;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initFenixEduClient();
	}

	private void initFenixEduClient() {
		Resources resources = getResources();
		String clientId = resources.getString(R.string.fenixedu_client_id);
		String clientSecret = resources
				.getString(R.string.fenixedu_client_secret);
		String baseUrl = resources.getString(R.string.fenixedu_client_base_url);
		String callbackUrl = resources
				.getString(R.string.fenixedu_client_callback_url);
		FenixEduConfig config = new FenixEduConfig(clientId, clientSecret, "",
				baseUrl, callbackUrl);
		fenixEduClient = FenixEduAndroidClientFactory.getSingleton(config);

	}

	public FenixEduAndroidClient getFenixEduClient() {
		return fenixEduClient;
	}

	public int getCurrentSelectedCampus() {
		return currentSelectedCampus;
	}

	public void setCurrentSelectedCampus(int currentSelectedCampus) {
		this.currentSelectedCampus = currentSelectedCampus;
	}

}
