package com.hetfotogeniekegeluid.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;

public class LocationStore {
	private ArrayList<Location> locations;
	private static LocationStore locationStore;

	private LocationStore() {
		locations = new ArrayList<Location>();
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	public void getLocation(int locationIndex) {

	}

	public void readLocations() {

	}

	/**
	 * Loads all the data into the application
	 */
	public void loadLocationStore() {
		locations.clear(); // make the list empty
		
		Gson gson = new Gson();
		URL url = getClass().getResource("locations.json");
		File file = new File(url.getPath());
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			locations.add(gson.fromJson(br, Location.class));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public static LocationStore getInstance() {
		if (locationStore == null)
			locationStore = new LocationStore();
		return locationStore;
	}
}
