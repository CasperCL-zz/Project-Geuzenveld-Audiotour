package com.hetfotogeniekegeluid.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;
import com.hetfotogeniekegeluid.model.LocationStore;
import com.hetfotogeniekegeluid.model.AudioService;
import com.hetfotogeniekegeluid.model.AudioService.LocalBinder;

/**
 * This is where is all starts.
 * 
 * @author Casper
 * 
 */
public class SplashActivity extends Activity {

	private MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ApplicationStatus.setContext(this);
		startSplash();
	}

	private void startSplash() {

		if (!isServiceRunning()) {
			Log.w("Messege", "Not running");
			setContentView(R.layout.activity_splash);
			player = MediaPlayer.create(this, R.raw.startup);
			player.setLooping(false); // Set looping
			player.start();
		
		Thread timer = new Thread() {

			@Override
			public void run() {

				try {

					//AudioManager.getInstance(SplashActivity.this)
						//	.loadAudioFiles();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				//} catch (IOException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				}
				// Load the predefined locations
				LocationStore.getInstance().loadLocationStore(
						SplashActivity.this);

				Intent mainActivity = new Intent(SplashActivity.this,
						MenuActivity.class);
			  startActivity(mainActivity);
			}
		};
		timer.start();
		}else{
			
			Log.w("Messege", "Running");
			LocationStore.getInstance().loadLocationStore(
					SplashActivity.this);
			Intent mainActivity = new Intent(SplashActivity.this,
					MenuActivity.class);
			startActivity(mainActivity);	
		}
	}

	/**
	 * Make sure we do not show this view again when we hit the back button.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			Log.w("Messege", service.service.getClassName());
			if ("com.hetfotogeniekegeluid.model.AudioService"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
