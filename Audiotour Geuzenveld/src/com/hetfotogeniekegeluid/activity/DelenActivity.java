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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ApplicationStatus.setContext(this);
		setContentView(R.layout.activity_delen);
		player = MediaPlayer.create(this, R.raw.click);
		
	}
	
	public void startShare(View v){
		player.start();
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Neem een audiotour door het prachtige Geuzenveld met deze handige app!";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Het gevoel van Geuzenveld");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	protected void onPause() {
		super.onPause();
		ApplicationStatus.activityPaused();
	};

	@Override
	protected void onResume() {
		super.onResume();
		ApplicationStatus.activityResumed();
	}
	
}
