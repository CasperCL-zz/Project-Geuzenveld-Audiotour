package com.hetfotogeniekegeluid.model;

import com.hetfotogeniekegeluid.activity.MapActivity;

import android.app.Application;
import android.content.Context;

public class ApplicationStatus extends Application {

	private static boolean activityVisible;
	private static MapActivity mapActivity;

	private static Context context;

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
	}

	public static void activityPaused() {
		activityVisible = false;
	}

	public static Context getLastContext() {
		return context;
	}

	public static void setContext(Context context) {
		ApplicationStatus.context = context;
	}

	public static MapActivity getMapActivity() {
		return mapActivity;
	}

	public static void setMapActivity(MapActivity mapActivity) {
		ApplicationStatus.mapActivity = mapActivity;
	}
}
