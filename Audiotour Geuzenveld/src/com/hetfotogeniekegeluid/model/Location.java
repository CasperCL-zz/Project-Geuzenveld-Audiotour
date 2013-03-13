package com.hetfotogeniekegeluid.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

public class Location {
	private String name;
	private String description;
	private double latitude;
	private double longitude;

	public Location(String name, String description, double latitude,
			double longitude) {
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public LatLng getLatLng() {
		return new LatLng(latitude, longitude);
	}

	public GeoPoint getGeoPoint() {
		return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	}
	
    @Override
    public String toString() {
        return "Location [name=" + name + ", description=" + description
                + "latitude=" + latitude + ", latitude=" + longitude + "]";
    }

	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
}