package com.hetfotogeniekegeluid.model;

public class Site extends Location {

	private String name;
	private String description;
	private boolean visited;

	public Site(String name, String description, double latitude,
			double longitude) {
		super(latitude, longitude, true);
		this.name = name;
		this.description = description;
		this.visited = false;
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

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isVisited() {
		return visited;
	}
}
