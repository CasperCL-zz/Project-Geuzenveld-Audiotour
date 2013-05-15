package com.hetfotogeniekegeluid.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

public class Location {

	private double latitude;
	private double longitude;
	private boolean isASite;
	
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		isASite = false;
	}
	
	public Location(double latitude, double longitude, boolean isASite) {
		this.latitude = latitude;
		this.longitude = longitude;
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