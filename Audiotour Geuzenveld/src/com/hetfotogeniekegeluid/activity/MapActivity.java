package com.hetfotogeniekegeluid.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;
import com.hetfotogeniekegeluid.model.LocationStore;
import com.hetfotogeniekegeluid.model.MenuItems;
import com.hetfotogeniekegeluid.model.Site;
import com.hetfotogeniekegeluid.service.AudioService;
import com.hetfotogeniekegeluid.service.AudioService.LocalBinder;
import com.hetfotogeniekegeluid.service.LocationUpdateService;

/**
 * This class initiates the map, and creates links to other activities.
 * 
 * @author Casper
 * 
 */
public class MapActivity extends FragmentActivity implements
		OnSeekBarChangeListener, OnMapClickListener, OnMarkerClickListener {

	private GoogleMap map;
	private ArrayList<Marker> markers;
	private LocationStore locationStore;
	private AudioService myAudioService;
	private LocationUpdateService mLocationUpdateService;
	private static SeekBar mSeekBar;
	private static Button mPausePlay;
	private static Handler mHandler = new Handler();
	private static boolean updateBar = true;
	private static View barPosition;
	private boolean barVisible = false;
	private static TextView durationText;
	private static boolean checkedForGPS = false;
	private boolean isBinded;
	private HashMap<Marker, Site> mMarkerMap = new HashMap<Marker, Site>();

	/**
	 * This happens on when the application starts.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationStatus.setContext(this);
		setContentView(R.layout.activity_map);
		isBinded = false;

		// Check for internet for the map
		if (checkInternet()) {
			// Check for GPS
			if (!checkedForGPS) {
				checkForGPS();
				checkedForGPS = true;
			}
			// create the map
			createMap();

			// Start the services
			// The audio service
			Intent audioService = new Intent(this, AudioService.class);
			startService(audioService);
			bindService(audioService, mConnection, Context.BIND_AUTO_CREATE);

			// The location service
			Intent locationUpdateService = new Intent(this,
					LocationUpdateService.class);
			startService(locationUpdateService);
			bindService(locationUpdateService, mLocationConnection,
					Context.BIND_AUTO_CREATE);
			isBinded = true;

			// create the links to the components of the xml page
			setupAudioBar();

			// Change the application status to this mapactivity.
			ApplicationStatus.setMapActivity(this);

			// Check if this activity has extra's, if so, we need to zoom in
			// detail.
			if (getIntent().getExtras() != null) {
				if (ApplicationStatus.getMapActivity() != null)
					ApplicationStatus.getMapActivity().finish();// finish the
																// previous map
																// activity in
																// order
																// to prevent
																// multiple
																// mapactivities.
				Location loc = (Location) getIntent().getExtras().get(
						"specific_zoom_location");

				// zoom in to the last know position
				LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Location myLocation = lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						myLocation.getLatitude(), myLocation.getLongitude()),
						18));
				locationStore.findSite(loc.getLatitude(), loc.getLongitude());
			} else {
				// Move the camera the default location
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						locationStore.getSite(0).getLatitude(), locationStore
								.getSite(0).getLongitude()), 15));
			}
		} else {
			// Show that you need internet to continue and quit to the main menu
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"U heeft momenteel geen actieve netwerkverbinding. De plattegrond zal niet weergeven worden.")
					.setCancelable(false);
			final AlertDialog alert = builder.create();
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Go to previous activity.
					MapActivity.this.finish();
				}
			});
			// Show the dialog
			alert.show();
		}

	}

	/**
	 * Create the links to the audiobar components.
	 */
	private void setupAudioBar() {
		// TODO Auto-generated method stub
		mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(this);
		mPausePlay = (Button) findViewById(R.id.pauseplay);
		durationText = (TextView) findViewById(R.id.audioText);

		barPosition = (View) findViewById(R.id.audioBar);
		barPosition.setVisibility(View.INVISIBLE);

		// Set the button image to the appropriate state
		setButtonImage();
	}

	/**
	 * Shows/hides the audio control bar.
	 */
	void addremoveBar() {
		if (barVisible) {
			barPosition.startAnimation(AnimationUtils.loadAnimation(this,
					R.drawable.slideout));
			barPosition.setVisibility(View.INVISIBLE);
			barVisible = false;
		} else {
			barPosition.startAnimation(AnimationUtils.loadAnimation(this,
					R.drawable.slidein));
			barPosition.setVisibility(View.VISIBLE);
			barVisible = true;
		}
	}

	/**
	 * Unbind the services from this activity.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBinded) {
			unbindService(mConnection);
			unbindService(mLocationConnection);
		}
	}

	/**
	 * Set the application status to pause (background).
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ApplicationStatus.activityPaused();

	}

	/**
	 * Set the application to front.
	 */
	protected void onResume() {
		super.onResume();
		ApplicationStatus.activityResumed();
		setButtonImage();
	};

	/**
	 * Start playing the audio from view
	 * 
	 * @param v
	 *            the view from the state is changed.
	 */
	public void audioStart(View v) {
		myAudioService.startstopAudio();
		setButtonImage();
	}

	/**
	 * Link the map with the one in XML and create the markers and the route.
	 */
	private void createMap() {
		markers = new ArrayList<Marker>();

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		map = mapFragment.getMap();
		map.setMyLocationEnabled(true);
		map.setIndoorEnabled(true);
		map.setOnMapClickListener(this);
		map.setOnMarkerClickListener(this);

		locationStore = LocationStore.getInstance();
		locationStore.loadLocationStore(this);

		createMarkers();
		createRoute();
	}

	/**
	 * Sets the button image to the appropriate image.
	 */
	private void setButtonImage() {
		if (myAudioService != null)
			if (myAudioService.checkPlaying()) {
				mPausePlay.setBackgroundResource(R.drawable.pause);
			} else {
				mPausePlay.setBackgroundResource(R.drawable.play);
			}
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
			rectOptions.color(0xBF3794D3);// 0x005DD49C
			// 0xff000000

			// add the route to the map
			Polyline polyline = map.addPolyline(rectOptions);
			return true;
		} else
			return false;
	}

	/**
	 * Create the markers from JSON.
	 * 
	 * @return succes
	 */
	private boolean createMarkers() {
		// Clear any old markers
		markers.clear();

		// Add the markers defined in the LocationStore.
		int counter = 1;
		for (Site place : locationStore.getSites()) {
			Display display = getWindowManager().getDefaultDisplay();
			BitmapDescriptor icon;

			if (display.getWidth() < 480) {
				icon = BitmapDescriptorFactory.fromAsset("icon/mdpi/m"
						+ counter++ + ".png");
			} else if (display.getWidth() < 720) {
				icon = BitmapDescriptorFactory.fromAsset("icon/mdpi/m"
						+ counter++ + ".png");
			} else {
				icon = BitmapDescriptorFactory.fromAsset("icon/mdpi/m"
						+ counter++ + ".png");
			}

			Marker tmpMarker = map.addMarker(new MarkerOptions()
			// .title(place.getName())
			// .snippet(place.getDescription())
					.position(place.getLatLng()).icon(icon));

			markers.add(tmpMarker);
			mMarkerMap.put(tmpMarker, place);

		}

		if (!markers.isEmpty())
			return true;
		return false;
	}

	/**
	 * Checks if internet is available, and if not, displays a message.
	 */
	private boolean checkInternet() {
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork == null
				|| activeNetwork.getState() != NetworkInfo.State.CONNECTED) {
			return false;
		}
		return true;
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

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// textAction.setText("starting to track touch");
		updateBar = false;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		myAudioService.setPos(seekBar.getProgress()); // seekBar.getProgress();
		// textAction.setText("ended tracking touch");
		updateBar = true;
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			myAudioService = binder.getService();

			// mBound = true;

			mHandler.postDelayed(moveSeekBarThread, 1000);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// mBound = false;
		}

	};

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mLocationConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocationUpdateService.LocalBinder binder = (LocationUpdateService.LocalBinder) service;
			mLocationUpdateService = binder.getService();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
		}

	};

	/**
	 * Keeps the seekbar up to date.
	 */
	private Runnable moveSeekBarThread = new Runnable() {
		private boolean startTxt = true;

		public void run() {

			if (myAudioService.getPos() + 100 > myAudioService.getDur()) {
				myAudioService.setPos(0);
				mSeekBar.setProgress(0);
				myAudioService.pauseAudio();
				mPausePlay.setBackgroundResource(R.drawable.play);

				int endSeconds = myAudioService.getDur() / 1000;
				int endMinutes = endSeconds / 60;
				endSeconds = endSeconds % 60;

				String endSec;
				if (endSeconds < 10) {
					endSec = "0" + String.valueOf(endSeconds);
				} else {
					endSec = String.valueOf(endSeconds);
				}

				durationText.setText(myAudioService.getCurrentTrackName() + "0"
						+ ":" + "00" + " / " + endMinutes + ":" + endSec);
			}

			if ((myAudioService.checkPlaying() && updateBar) || startTxt) {
				startTxt = false;
				mSeekBar.setMax(myAudioService.getDur());
				mSeekBar.setProgress(myAudioService.getPos());

				int curSeconds = myAudioService.getPos() / 1000;
				int curMinutes = curSeconds / 60;
				curSeconds = curSeconds % 60;
				int endSeconds = myAudioService.getDur() / 1000;
				int endMinutes = endSeconds / 60;
				endSeconds = endSeconds % 60;

				String curSec, endSec;
				if (curSeconds < 10) {
					curSec = "0" + String.valueOf(curSeconds);
				} else {
					curSec = String.valueOf(curSeconds);
				}

				if (endSeconds < 10) {
					endSec = "0" + String.valueOf(endSeconds);
				} else {
					endSec = String.valueOf(endSeconds);
				}

				durationText.setText(myAudioService.getCurrentTrackName()
						+ curMinutes + ":" + curSec + " / " + endMinutes + ":"
						+ endSec);
			}
			mHandler.postDelayed(this, 100); // Looping the thread after 0.1
												// second
			// seconds
		}

	};

	/**
	 * Called when clicked on the map.
	 */
	@Override
	public void onMapClick(LatLng point) {
		addremoveBar();
	}

	/**
	 * Shows a pop-up for a specific site.
	 * @param site of the popup.
	 */
	private void showPopup(final Site site) {
		ViewGroup popup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.popup_map, null);
		final PopupWindow popupWindow = new PopupWindow(popup,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(mSeekBar, Gravity.CENTER, 0, 0);

		((TextView) popup.findViewById(R.id.textViewTitle)).setText(site
				.getName());
		((TextView) popup.findViewById(R.id.textViewExtra)).setText(site
				.getDescription());

		int imageResource = getResources().getIdentifier(site.getImage(),
				"drawable", getPackageName());
		((ImageView) popup.findViewById(R.id.imageViewLocation))
				.setImageResource(imageResource);

		popup.findViewById(R.id.imagePopupPlay).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!barVisible)
							addremoveBar();
						myAudioService.setFileNr(locationStore.getSites().indexOf(site) + 1);
						myAudioService.makePlayer();
						// popupWindow.dismiss();
						audioStart(v);
					}
				});
	}

	/**
	 * Called when clicked on a marker
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		showPopup(mMarkerMap.get(marker));
		return false;
	}
}
