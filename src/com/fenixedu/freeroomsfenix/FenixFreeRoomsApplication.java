package com.fenixedu.freeroomsfenix;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixedu.android.FenixEduAndroidClient;
import pt.ist.fenixedu.android.FenixEduAndroidClientFactory;
import pt.ist.fenixedu.sdk.FenixEduConfig;
import pt.ist.fenixedu.sdk.beans.publico.FenixSpace;
import android.app.Application;
import android.content.res.Resources;

public class FenixFreeRoomsApplication extends Application {

	FenixEduAndroidClient fenixEduClient;

	private int currentSelectedCampusPosition;
	private FenixSpace currentCampus;
	private DateTime time;

	public FenixFreeRoomsApplication() {
		currentSelectedCampusPosition = -1;
		time = new DateTime();
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
		FenixEduAndroidClientFactory.setClientConfig(config);
		fenixEduClient = FenixEduAndroidClientFactory.getInstance();
	}

	public FenixEduAndroidClient getFenixEduClient() {
		return fenixEduClient;
	}

	public int getCurrentSelectedCampusPosition() {
		return currentSelectedCampusPosition;
	}

	public void setCurrentSelectedCampusPosition(int currentSelectedCampus) {
		this.currentSelectedCampusPosition = currentSelectedCampus;
	}

	public FenixSpace getCurrentCampus() {
		return currentCampus;
	}

	public void setCurrentCampus(FenixSpace currentCampus) {
		this.currentCampus = currentCampus;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

	public String getTimeAsString() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		String dateString = formatter.print(time);
		return dateString;
	}

}
