package com.hetfotogeniekegeluid.activity;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class InstructionMenuActivity extends Activity {

	private MediaPlayer player;

	// The media player for the clicking sound

	// This function is called when the activity is initialized
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Call the onCreate in Activity
		ApplicationStatus.setContext(this);
		// Let the AppStatus know we are in the instruction activity
		setContentView(R.layout.activity_instruction_main);
		// Load the correct layout
		player = MediaPlayer.create(this, R.raw.click);
		// Initialize the media player
	}
	
	// This function is called when the volgende/next button is pressed
	public void onNextClick(View v) {
		player.start();
		// Play the clicking sound
		setContentView(R.layout.activity_instruction_second);
		// Load the second instruction layout
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
