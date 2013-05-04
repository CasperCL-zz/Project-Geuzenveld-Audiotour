package com.hetfotogeniekegeluid.activity;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ApplicationStatus;
import com.hetfotogeniekegeluid.model.AudioService;
import com.hetfotogeniekegeluid.model.AudioService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MenuActivity extends Activity {
	
	private MediaPlayer player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ApplicationStatus.setContext(this);
		setContentView(R.layout.activity_menu);
		player = MediaPlayer.create(this, R.raw.click);
	}

	public void onStartClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, MapActivity.class));
	}

	public void onInstructionClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, InstructionMenuActivity.class));
	}

	public void onColofonClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, ColofonActivity.class));
	}
	
	public void onDelenClick(View v) {
		player.start();
		startActivity(new Intent(MenuActivity.this, DelenActivity.class));
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
