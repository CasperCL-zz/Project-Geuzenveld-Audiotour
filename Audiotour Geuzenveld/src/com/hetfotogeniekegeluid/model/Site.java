package com.hetfotogeniekegeluid.model;

/**
 * A site where the user of the application can listen to audio and watch a picture.
 * @author Casper
 *
 */
public class Site extends Location {

	// The name of the site
	private String name;
	// The description of the site
	private String description;
	// The image from the site
	private String image;
	// a check whether the user visited this site before.
	private boolean visited;
	// the audio file nr
	private int audioFileNr;

	/**
	 * Constructs a Site object.
	 * 
	 * @param name of the site
	 * @param description of the site
	 * @param image	of the site
	 * @param latitude	of the site
	 * @param longitude	of the site
	 */
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

	/**
	 * Used to read the JSON in objects.
	 */
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
		return audioFileNr;
	}
}
