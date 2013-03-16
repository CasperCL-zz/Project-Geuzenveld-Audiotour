package com.hetfotogeniekegeluid.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.OverlayItem;
import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.LocationStore;
import com.hetfotogeniekegeluid.model.MenuItems;
import com.hetfotogeniekegeluid.model.Site;

/**
 * This class initiates the map, and creates links to other activities.
 * 
 * @author Casper
 * 
 */
public class MapActivity extends FragmentActivity implements LocationListener {

	private GoogleMap map;
	private ArrayList<Marker> markers;
	private LocationStore locationStore;
	private ArrayList<OverlayItem> mapOverlays;

	/**
	 * This happens on when the application starts.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mapOverlays = new ArrayList<OverlayItem>();
		markers = new ArrayList<Marker>();

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		locationStore = LocationStore.getInstance();

		// Check for Internet
		checkInternet();
		// Check for GPS
		checkForGPS();
		// Load the predefined locations
		locationStore.loadLocationStore(this);
		createMarkers();
		createRoute();

		// Move the map to the view
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.380498,
				4.802291), 15));
	}

	/**
	 * Draws a route on the map from the registered markers.
	 * 
	 * @return false when there are no markers from which the route can be drawn
	 *         from.
	 */
	private boolean createRoute() {
		if (!markers.isEmpty()) {
			// Instantiates a new Polyline object and adds points to define a
			// rectangle
			PolylineOptions rectOptions = new PolylineOptions();

			for (com.hetfotogeniekegeluid.model.Location location : locationStore
					.getLocations()) {
				rectOptions.add(location.getLatLng());
			}
			rectOptions.color(0xBF5DD49C);// 0x005DD49C
			// 0xff000000

			// add the route to the map
			Polyline polyline = map.addPolyline(rectOptions);
			return true;
		} else
			return false;
	}

	/**
	 * Checks if internet is available, and if not, displays a message.
	 */
	private void checkInternet() {
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork == null
				|| activeNetwork.getState() != NetworkInfo.State.CONNECTED) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"U heeft momenteel geen actieve netwerkverbinding. De plattegrond zal niet goed weergeven worden.")
					.setCancelable(false).setPositiveButton("OK", null);
			final AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public Bitmap drawTextToBitmap(Context mContext, int resourceId,
			String mText) {
		try {
			Resources resources = mContext.getResources();
			float scale = resources.getDisplayMetrics().density;
			Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

			android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
			// set default bitmap config if none
			if (bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);

			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// text color - #3D3D3D
			paint.setColor(Color.rgb(110, 110, 110));
			// text size in pixels
			paint.setTextSize((int) (12 * scale));
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(mText, 0, mText.length(), bounds);
			int x = (bitmap.getWidth() - bounds.width()) / 6;
			int y = (bitmap.getHeight() + bounds.height()) / 5;

			canvas.drawText(mText, x * scale, y * scale, paint);

			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private boolean createMarkers() {
		// Clear any old markers
		markers.clear();

		// Add the markers defined in the LocationStore.
		int counter = 1;
		for (Site place : locationStore.getSites()) {
			Display display = getWindowManager().getDefaultDisplay();
			BitmapDescriptor icon;

			if (display.getWidth() < 480) {
				icon = BitmapDescriptorFactory
						.fromAsset("icon/mdpi/m" + counter++ + ".png");
			} else if (display.getWidth() < 720) {
				icon = BitmapDescriptorFactory
						.fromAsset("icon/mdpi/m" + counter++ + ".png");
			} else {
				icon = BitmapDescriptorFactory
						.fromAsset("icon/mdpi/m" + counter++ + ".png");
			}

			Marker tmpMarker = map.addMarker(new MarkerOptions()
					.title(place.getName())
					.snippet(place.getDescription())
					.position(place .getLatLng()).icon(icon));

			markers.add(tmpMarker);
			
		}

		if (!markers.isEmpty())
			return true;
		return false;
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
		if (item.getTitle().equals(MenuItems.Colofon.toString())) {
			selectedActivity = new Intent(MapActivity.this,
					ColofonActivity.class);
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
