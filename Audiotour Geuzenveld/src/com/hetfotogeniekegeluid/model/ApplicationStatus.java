package com.hetfotogeniekegeluid.model;

import com.hetfotogeniekegeluid.activity.MapActivity;

import android.app.Application;
import android.content.Context;

/**
 * This object monitors the application status.
 * @author Casper
 *
 */
public class ApplicationStatus extends Application {

	// True if the application is running in the foreground.
	private static boolean activityVisible;
	// The current MapActivity object
	private static MapActivity mapActivity;
	// The current contex
	private static Context context;

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	/**
	 * Needs to be called as soon as the application goes into foreground.
	 */
	public static void activityResumed() {
		activityVisible = true;
	}

	/**
	 * Needs to be called as soon as the application goes into background.
	 */
	public static void activityPaused() {
		activityVisible = false;
	}

	/**
	 * Get the last context of the application.
	 * @return the last application context
	 */
	public static Context getLastContext() {
		return context;
	}

	/** Sets the a new context.
	 * 
	 * @param context of now
	 */
	public static void setContext(Context context) {
		ApplicationStatus.context = context;
	}

	/**
	 * Get the last MapActivity context.
	 * 
	 * @return
	 */
	public static MapActivity getMapActivity() {
		return mapActivity;
	}

	/**
	 * Set a new MapActivity object.
	 * 
	 * @param mapActivity
	 */
	public static void setMapActivity(MapActivity mapActivity) {
		ApplicationStatus.mapActivity = mapActivity;
	}
}
