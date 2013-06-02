package com.hetfotogeniekegeluid.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.IStub;
import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;
import com.hetfotogeniekegeluid.model.LocationStore;
import com.hetfotogeniekegeluid.model.ObbExpansionsManager;
import com.hetfotogeniekegeluid.model.ObbExpansionsManager.ObbListener;
import com.hetfotogeniekegeluid.service.AudioDownloadService;

/**
 * This is where is all starts.
 * 
 * @author Casper
 * 
 */
public class SplashActivity extends Activity {

	private MediaPlayer player;
	private boolean hasExpansionFiles;

	// The media player for the startup sound

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// This function is called when the activity is initialized
		super.onCreate(savedInstanceState);
		hasExpansionFiles = false;
		// Call the onCreate in Activity
		ApplicationStatus.setContext(this);
		// Let the AppStatus know we are in the splash activity
		startSplash();
	}

	private void startSplash() {

		// First we check if the AudioService is running or not
		if (!isServiceRunning()) {
			// If it is not running
			setContentView(R.layout.activity_splash);
			// Load the correct layout
			player = MediaPlayer.create(this, R.raw.startup);
			// Initialize the media player
			player.setLooping(false);
			// Set looping to false
			// Play the startup sound

			// Load the audio files
			ObbExpansionsManager.createNewInstance(this, new ObbListener() {

				@Override
				public void onMountSuccess() {
					// don't do anything.. this should be normal ;)
					hasExpansionFiles = true;
					player.start();
				}

				@Override
				public void onFilesNotFound() {
					// Download the obb files
					hasExpansionFiles = false;
					SplashActivity.this.downloadAudioFiles();
				}
			});

			Thread timer = new Thread("Splash Waiting Thread") {
				// Create a new thread

				@Override
				public void run() {
					LocationStore.getInstance().loadLocationStore(
							SplashActivity.this);

					// Load the predefined locations
					try {
						Thread.sleep(6000);
						// Wait for 6 seconds
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (hasExpansionFiles) {
						Intent mainActivity = new Intent(SplashActivity.this,
								MenuActivity.class);
						startActivity(mainActivity);
						// Create a new Intent, pass MenuActivity to it, then
						// start
						// it
					}
				}
			};
			timer.start();
			// Start the thread
		} else {
			// If the AudioService is already running
			LocationStore.getInstance().loadLocationStore(SplashActivity.this);
			// Load the predefined locations
			Intent mainActivity = new Intent(SplashActivity.this,
					MenuActivity.class);
			startActivity(mainActivity);
			// Create a new Intent, pass MenuActivity to it, then start it
		}
	}

	protected void downloadAudioFiles() {
		// let the AudioDownloadActivity handle the rest
		startActivity(new Intent(this, AudioDownloadActivity.class));
		finish();
	}

	/**
	 * Make sure we do not show this view again when we hit the back button.
	 */
	// Is called when the activity is paused, but not deleted
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	// This function checks if the AudioService is running
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// Create a new ActivityManager so we can access the running services
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			// Loop through all running services
			if ("com.hetfotogeniekegeluid.service.AudioService"
					.equals(service.service.getClassName())) {
				// Check if the current name matches our AudioService
				return true;
			}
		}
		return false;
	}
}
