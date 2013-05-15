package com.hetfotogeniekegeluid.service;

import com.hetfotogeniekegeluid.R;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AudioService extends Service {
	private static final String TAG = "Audiotour_AudioService";
	private final IBinder mBinder = new LocalBinder();
	private int fileNr = 1;
	private MediaPlayer player;
	private String currentTrackName;

	public class LocalBinder extends Binder {

		public AudioService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return AudioService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		makePlayer();
		player.setLooping(false); // Set looping
	}

	@Override
	public void onDestroy() {
		//player.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {

	}

	public void setFileNr(int fileNr) {
		this.fileNr = fileNr;
	}

	public void startstopAudio() {
		if (player.isPlaying()) {
			player.pause();
		} else {
			player.start();
		}
	}

	public void pauseAudio() {
		if (player == null)
			return;
		player.pause();
	}

	public void makePlayer() {
		if (player != null) {
			player.stop();
			player = null;
		}
		switch (fileNr) {
		case 1:
			player = MediaPlayer.create(this, R.raw.file1);
			currentTrackName = "1: Op Weg ";
			break;
//		case 2:
//			player = MediaPlayer.create(this, R.raw.file2);
//			currentTrackName = "2: Hart van Geuzenveld ";
//			break;
//		case 3:
//			player = MediaPlayer.create(this, R.raw.file3);
//			currentTrackName = "3: Leven in een Bouwput ";
//			break;
//		case 4:
//			player = MediaPlayer.create(this, R.raw.file4);
//			currentTrackName = "4: De eerste jaren ";
//			break;
//		case 5:
//			player = MediaPlayer.create(this, R.raw.file5);
//			currentTrackName = "5: Smeltkroes ";
//			break;
//		case 6:
//			player = MediaPlayer.create(this, R.raw.file6);
//			currentTrackName = "6: Verzet tegen sloop ";
//			break;
//		case 7:
//			player = MediaPlayer.create(this, R.raw.file7);
//			currentTrackName = "7: Nieuwe Tijden ";
//			break;
//		case 8:
//			player = MediaPlayer.create(this, R.raw.file8);
//			currentTrackName = "8: Voetbaldromen ";
//			break;
//		case 9:
//			player = MediaPlayer.create(this, R.raw.file9);
//			currentTrackName = "9: Roomse inslag ";
//			break;
//		case 10:
//			player = MediaPlayer.create(this, R.raw.file10);
//			currentTrackName = "10: Leren wonen ";
//			break;
//		case 11:
//			player = MediaPlayer.create(this, R.raw.file11);
//			currentTrackName = "11: Geuzennaam ";
//			break;
//		case 12:
//			player = MediaPlayer.create(this, R.raw.file12);
//			currentTrackName = "12: Kunstgreep ";
//			break;
//		case 13:
//			player = MediaPlayer.create(this, R.raw.file13);
//			currentTrackName = "13: Bakkie troost ";
//			break;
//		case 14:
//			player = MediaPlayer.create(this, R.raw.file14);
//			currentTrackName = "14: Hoog niveau ";
//			break;
//		case 15:
//			player = MediaPlayer.create(this, R.raw.file15);
//			currentTrackName = "15: Naar huis ";
//			break;

		}

	}

	public String getCurrentTrackName() {
		return currentTrackName;
	}

	public boolean checkPlaying() {
		if (player == null)
			return false;
		return player.isPlaying();
	}

	public boolean checkNull() {
		if (player == null) {
			return true;
		} else {
			return false;
		}
	}

	public int getPos() {
		if (player == null)
			return 0;
		return player.getCurrentPosition();
	}

	public void setPos(int pos) {
		if (player == null)
			return;
		player.seekTo(pos);
	}

	public int getDur() {
		if (player == null)
			return 0;
		return player.getDuration();
	}

	public static RunningServiceInfo isRunning(Context c) {
		ActivityManager manager = (ActivityManager) c
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (AudioService.class.getName().equals(
					service.service.getClassName())) {
				Log.w("DEBUG", "SERVICE RUNNING");
				return service;
			}
		}
		Log.w("DEBUG", "SERVICE NOT RUNNING");
		return null;
	}

}
