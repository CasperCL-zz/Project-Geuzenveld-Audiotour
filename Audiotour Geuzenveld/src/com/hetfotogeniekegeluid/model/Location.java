package com.hetfotogeniekegeluid.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

public class Location {
	private LatLng latLng;
	private GeoPoint geoPoint;

	public Location(double latitude, double longitude) {
		new LatLng(latitude, longitude);
	}

	@Override
	public String toString() {
		return "Location [latLng=" + latLng + ", geoPoint=" + geoPoint + "]";
	}
}