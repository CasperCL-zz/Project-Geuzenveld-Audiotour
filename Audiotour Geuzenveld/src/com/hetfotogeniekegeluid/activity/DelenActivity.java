package com.hetfotogeniekegeluid.activity;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class DelenActivity extends Activity {

	private MediaPlayer player;

	// The media player for the clicking sound

	// This function is called when the activity is initialized
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Call the onCreate in Activity
		ApplicationStatus.setContext(this);
		// Let the AppStatus know we are in the delen activity
		setContentView(R.layout.activity_delen);
		// Load the correct layout
		player = MediaPlayer.create(this, R.raw.click);
		// Initialize the media player
	}

	// Function that is called when the share button is pressed
	public void startShare(View v) {
		player.start();
		// Play the clicking sound
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		// Create a new Intent that we can send to another person (as a message)
		sharingIntent.setType("text/plain");
		// Tell the intent that we are sending text
		String shareBody = "Neem een audiotour door het prachtige Geuzenveld met deze handige app!";
		// This holds the actual text we are sending
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Het gevoel van Geuzenveld");
		// This sets the subject of the message
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		// This links the text we made to our intent
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
		// Start the activity
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
