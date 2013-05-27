package com.hetfotogeniekegeluid.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;

public class MenuActivity extends Activity {

	private MediaPlayer player;

	// The media player for the clicking sound

	// This function is called when the activity is initialized
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Call the onCreate in Activity
		ApplicationStatus.setContext(this);
		// Let the AppStatus know we are in the menu activity
		setContentView(R.layout.activity_menu);
		// Load the correct layout
		player = MediaPlayer.create(this, R.raw.click);
		// Initialize the media player
	}

	// This function is called when the Audiotour button is pressed
	public void onStartClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, MapActivity.class));
		//Start the MapActivity by passing it to a new Intent
	}

	// This function is called when the Instructies button is pressed
	public void onInstructionClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this,
				InstructionMenuActivity.class));
		//Start the InstructionMenuActivity by passing it to a new Intent
	}

	// This function is called when the Colofon button is pressed
	public void onColofonClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, ColofonActivity.class));
		//Start the ColofonActivity by passing it to a new Intent
	}

	// This function is called when the Delen button is pressed
	public void onDelenClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, DelenActivity.class));
		//Start the DelenActivity by passing it to a new Intent
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
