package com.hetfotogeniekegeluid.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class is used to encapsulate the location.
 * @author Casper
 *
 */
public class Location {

	// The latitude
	private double latitude;
	// The longitude
	private double longitude;
	// In order to implement JSON isASite was addded
	private boolean isASite;
	
	/**
	 * Construct a location based on a latitude and a longitude.
	 * @param latitude of the location
	 * @param longitude of the location
	 */
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		isASite = false;
	}
	
	/**
	 * Construct a location based on a latitude and a longitude.
	 * @param latitude of the location
	 * @param longitude of the location
	 * @param isASite check for is a site
	 */
	public Location(double latitude, double longitude, boolean isASite) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.isASite = isASite;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public LatLng getLatLng() {
		return new LatLng(latitude, longitude);
	}

	public boolean isASite() {
		return isASite;
	}
}