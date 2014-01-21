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
	private int currentSelectedBuildingPosition;
	private FenixSpace currentBuilding;

	private static final String datePattern = "dd/MM/yyyy";
	private static final String hourPattern = "HH:mm";

	private int searchHour;
	private int searchMinute;

	public int getCurrentSelectedBuildingPosition() {
		return currentSelectedBuildingPosition;
	}

	public void setCurrentSelectedBuildingPosition(
			int currentSelectedBuildingPosition) {
		this.currentSelectedBuildingPosition = currentSelectedBuildingPosition;
	}

	public FenixSpace getCurrentBuilding() {
		return currentBuilding;
	}

	public void setCurrentBuilding(FenixSpace currentBuilding) {
		this.currentBuilding = currentBuilding;
	}

	private DateTime time;

	public FenixFreeRoomsApplication() {
		currentSelectedCampusPosition = -1;
		time = new DateTime();
		this.searchHour = time.getHourOfDay();
		this.searchMinute = time.getMinuteOfHour();
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

	public String getDateAsString() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
		String dateString = formatter.print(time);
		return dateString;
	}

	public String getDateTimePattern() {
		return datePattern + " " + hourPattern;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public int getSearchHour() {
		return searchHour;
	}

	public void setSearchHour(int searchHour) {
		this.searchHour = searchHour;
	}

	public int getSearchMinute() {
		return searchMinute;
	}

	public void setSearchMinute(int searchMinute) {
		this.searchMinute = searchMinute;
	}

	public String getSearchTimeAsString() {
		String hour, minute;
		if (this.searchHour < 10) {
			hour = "0" + this.searchHour;
		} else {
			hour = "" + this.searchHour;
		}

		if (this.searchMinute < 10) {
			minute = "0" + this.searchMinute;
		} else {
			minute = "" + this.searchMinute;
		}
		return hour + ":" + minute;
	}

}
