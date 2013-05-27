package com.hetfotogeniekegeluid.activity;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class ColofonActivity extends Activity {

	private MediaPlayer player;

	// The media player for the clicking sound

	// This function is called when the activity is initialized
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Call the onCreate in Activity
		ApplicationStatus.setContext(this);
		// Let the AppStatus know we are in the colofon
		setContentView(R.layout.activity_colofon);
		// Load the correct layout
		player = MediaPlayer.create(this, R.raw.click);
		// Initialize the media player
	}

	// This function opens the website of Geuzenveld
	public void openWebsiteGvG(View v) {
		player.start();
		// Play the clicking sound
		Intent intent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://www.nieuwwest.amsterdam.nl/wonen_en/de-9-wijken-van/geuzenveld"));
		// Create a new intent to be displayed and give it the web adress
		startActivity(intent);
		// Start the just created intent, which opens the website in a new
		// window
	}

	public void openWebsitehFG(View v) {
		player.start();
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.hetfotogeniekegeluid.nl/"));
		// Same function, different web adress
		startActivity(intent);
	}

	// Is called when the activity is paused, but not deleted
	@Override
	protected void onPause() {
		super.onPause();
		ApplicationStatus.activityPaused();
	};

	// Is called when the activity is resumed
	@Override
	protected void onResume() {

		super.onResume();
		ApplicationStatus.activityResumed();
	}
}
