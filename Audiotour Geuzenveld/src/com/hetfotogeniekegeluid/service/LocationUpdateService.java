package com.hetfotogeniekegeluid.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.activity.MapActivity;
import com.hetfotogeniekegeluid.model.ApplicationStatus;
import com.hetfotogeniekegeluid.model.LocationStore;
import com.hetfotogeniekegeluid.model.Site;

public class LocationUpdateService extends Service {

	// The service binder
	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {

		public LocationUpdateService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return LocationUpdateService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// Return this instance of LocalService so clients can call public
		// methods
		return mBinder;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		// clean up the mess
	}

	@Override
	public void onStart(Intent intent, int startid) {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, // 5-second
																		// interval.
				10, // 10 meters.
				listener);
	}

	/**
	 * The location listener.
	 */
	private final LocationListener listener = new LocationListener() {

		/**
		 * Called when the user changes from location.
		 */
		@Override
		public void onLocationChanged(Location myLocation) {
			
			double closest = 90000;
			int counter = 1;
			// Go over every site and determine which is the closest to the user.
			for (Site site : LocationStore.getInstance().getSites()) {
				// Calculate the distance
				double dist = distFrom(myLocation, site);
				if (dist < closest) {
					closest = dist;
				}
				
				// If the distance is smaller than 30 meters and it has not been visited before
				if (dist < 30 && !site.isVisited()) { // 20 meters near a
														// site and not
														// visited (this
														// prevents spam)

					site.setVisited(true);

					// Show a popup when in app
					if (ApplicationStatus.isActivityVisible()) {
						final AlertDialog alertDialog = new AlertDialog.Builder(
								ApplicationStatus.getLastContext()).create();
						alertDialog.setTitle("");
						alertDialog.setMessage("Je bent bij een luisterplek!"
								+ counter + ". " + site.getName());
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										alertDialog.dismiss();
									}
								});
						alertDialog.show();
					} else { // create a notification
						Location l = new Location(site.getName());
						l.setLatitude(site.getLatitude());
						l.setLongitude(site.getLongitude());
						Log.w("LUS", site.getName() + ": " + l.getLatitude()
								+ ", " + l.getLongitude());
						createNewLocationNotifi(site.getName(), l);
					}
				}
				counter++;
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	};


	public double distFrom(Location location1,
			com.hetfotogeniekegeluid.model.Location loc) {
		double lat1 = location1.getLatitude();
		double lng1 = location1.getLongitude();

		double lat2 = loc.getLatitude();
		double lng2 = loc.getLongitude();

		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (dist * meterConversion);
	}

	/**
	 * Creates a notification for the user, linked to a specific zoom location for the MapActivity.
	 * @param siteName the name of the site
	 * @param location  of the site
	 */
	private void createNewLocationNotifi(String siteName, Location location) {
		// Prepare intent which is triggered if the
		// notification is selected

		Intent intent = new Intent(LocationUpdateService.this,
				MapActivity.class);
		intent.putExtra("specific_zoom_location", location);

		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// Build notification
		// Actions are just fake
		Notification noti = new NotificationCompat.Builder(this)
				.setContentTitle("Audiotour: " + siteName)
				.setContentText("U bent in de buurt van een punt " + siteName)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText("U bent in de buurt van "
										+ siteName
										+ ". Open de app om het fragment af te spelen."))
				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_LIGHTS
								| Notification.DEFAULT_VIBRATE).build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);
	}
}
