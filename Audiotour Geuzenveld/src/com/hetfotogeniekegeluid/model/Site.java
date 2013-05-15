package com.hetfotogeniekegeluid.model;

public class Site extends Location {

	private String name;
	private String description;
	private String image;
	private boolean visited;
	private int audioNr;

	public Site(String name, String description, String image, double latitude,
			double longitude) {
		super(latitude, longitude, true);
		this.name = name;
		this.description = description;
		this.image = image;
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

	public String getImage() {
		return image;
	}

	public int getAudioFileNr() {
		return audioNr;
	}

}
