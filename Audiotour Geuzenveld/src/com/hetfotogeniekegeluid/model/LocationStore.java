package com.hetfotogeniekegeluid.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
		InputStream inputStream = getClass().getResourceAsStream("locations.json");
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			
	        String in;
	        Location tmpLoc;
	        while ((in = br.readLine()) != null) {
	        	tmpLoc = gson.fromJson(in, Location.class);
	        	locations.add(tmpLoc);
	        }

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
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
