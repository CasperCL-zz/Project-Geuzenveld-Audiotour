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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ApplicationStatus.setContext(this);

		setContentView(R.layout.activity_colofon);
		player = MediaPlayer.create(this, R.raw.click);
	}

	public void openWebsiteGvG(View v) {
		player.start();
		Intent intent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://www.nieuwwest.amsterdam.nl/wonen_en/de-9-wijken-van/geuzenveld"));
		startActivity(intent);
	}

	public void openWebsitehFG(View v) {
		player.start();
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.hetfotogeniekegeluid.nl/"));
		startActivity(intent);
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
