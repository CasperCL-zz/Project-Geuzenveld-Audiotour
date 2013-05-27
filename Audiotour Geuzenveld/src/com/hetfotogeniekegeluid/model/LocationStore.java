package com.hetfotogeniekegeluid.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;

import com.google.gson.Gson;


/**
 * A store for all the location objects.
 * @author Casper
 *
 */
public class LocationStore {
	// The locations
	private ArrayList<Location> locations;
	// The sites
	private ArrayList<Site> sites;
	// The singleton object
	private static LocationStore locationStore;

	private LocationStore() {
		locations = new ArrayList<Location>();
		sites = new ArrayList<Site>();
	}

	/**
	 * Loads all the data into the application
	 */
	public void loadLocationStore(Activity context) {
		locations.clear(); // make the list empty
		sites.clear();

		Gson gson = new Gson();
		InputStream is = null;
		BufferedReader br = null;

		try {
			is = context.getAssets().open("location/locations.json");
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String in;
			Site tmpSite;
			while ((in = br.readLine()) != null) {
				tmpSite = gson.fromJson(in, Site.class);

				// If it is a location, mark it for setting the marker later on
				if (tmpSite.isASite())
					sites.add(tmpSite);
				// Add the Location (which can also be a Site) to the locations
				// list so a route can be drawn.
				locations.add(tmpSite);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
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

	public Site getSite(int i) {
		return sites.get(i);
	}

	public ArrayList<Site> getSites() {
		return sites;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	/**
	 * Find a site in the store with a latitude and a longitude.
	 * @param latitude of the site
	 * @param longitude of the site
	 * @return the site, null if no site was found
	 */
	public Site findSite(double latitude, double longitude) {
		for(Site s: sites){
			if(s.getLatitude() == latitude && s.getLongitude() == s.getLongitude()){
				return s;
			}
		}
		return null;
	}
}
