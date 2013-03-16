package com.hetfotogeniekegeluid.model;

public class Site extends Location {

	private String name;
	private String description;

	public Site(String name, String description, double latitude,
			double longitude) {
		super(latitude, longitude, true);
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Site [name=" + name + ", description=" + description
				+ "latitude=" + getLatitude() + ", longitude=" + getLongitude()
				+ "]";
	}

}
