package com.hetfotogeniekegeluid.activity;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.OverlayItem;
import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.LocationStore;
import com.hetfotogeniekegeluid.model.MenuItems;

/**
 * This class is the first class after the SplashScreen executed on the app. It
 * initiates the map, and creates links to other activities.
 * 
 * @author Casper
 * 
 */
public class MainActivity extends FragmentActivity implements LocationListener {

	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	private GoogleMap map;
	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private Location myLocation;
	private LocationStore locationStore;

	/**
	 * This happens on when the application starts.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);

		Marker marker = map.addMarker(new MarkerOptions()
				.position(KIEL)
				.title("Kiel")
				.snippet("Kiel is cool")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));
		locationStore = LocationStore.getInstance();

		// Check for GPS
		checkForGPS();
		// Load the predefined locations (Still needs some fixing)
//		locationStore.loadLocationStore();

		// Move the map to the view
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.380498,
				4.802291), 15));
	}

	/**
	 * Checks whether GPS is enabled or not. If not it will prompt a dialog to
	 * set it on.
	 */
	public void checkForGPS() {
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings

		if (!enabled) {
			final AlertDialog alertDialog = new AlertDialog.Builder(this)
					.create();
			alertDialog.setTitle("GPS");
			alertDialog.setMessage("GPS staat uit. Wil je dit aanzetten?");
			alertDialog.setButton("Ja", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
			});
			alertDialog.setButton2("Nee",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.dismiss();
						}
					});

			alertDialog.show();

		}
	}

	/**
	 * Creates the option menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		Intent selectedActivity = null;
		if(item.getTitle().equals(MenuItems.Colofon.toString())){
			selectedActivity = new Intent(MainActivity.this, ColofonActivity.class);
		}

		startActivity(selectedActivity);
		return super.onOptionsItemSelected(item);
	}

	/**
	 * LocationListener methods.
	 */

	/**
	 * 
	 */
	@Override
	public void onLocationChanged(Location location) {
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),
				15));
	}

	/**
	 * 
	 */
	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	/**
	 * 
	 */
	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
