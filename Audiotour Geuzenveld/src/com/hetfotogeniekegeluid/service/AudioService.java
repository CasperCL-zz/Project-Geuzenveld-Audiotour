package com.hetfotogeniekegeluid.service;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.storage.StorageManager;
import android.util.Log;

import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.model.ObbExpansionsManager;
import com.hetfotogeniekegeluid.model.ObbExpansionsManager.ObbListener;

public class AudioService extends Service {
	// The service binder
	private final IBinder mBinder = new LocalBinder();
	// Object that allows us to bind the service to an activity
	private int fileNr = 1;
	// Tells the media player what file to use
	private MediaPlayer player;
	// The media player
	private String trackName;

	// The name of the audio file

	// Class for clients to access
	public class LocalBinder extends Binder {

		public AudioService getService() {
			// Return this instance of AudioService so clients can call public
			// methods
			return AudioService.this;
		}
	}

	// Function that returns the IBinder, so activities can bind to the service
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This function is called when the service is initialized
	@Override
	public void onCreate() {
		// Create the media player
		makePlayer();
		player.setLooping(false);
		// Set looping
	}

	@Override
	public void onDestroy() {
		player.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {

	}

	// Function that sets the track number
	public void setFileNr(int fileNr) {
		this.fileNr = fileNr;
	}

	// Function that Plays/Pauses the media player
	public void startstopAudio() {
		if (player.isPlaying()) {
			player.pause();
		} else {
			player.start();
		}
	}

	// Function that explicitly pauses the audio
	public void pauseAudio() {
		if (player == null)
			return;
		player.pause();
	}

	// Function that creates the audio player
	public void makePlayer() {
		//If needed, stop the player and empty it
		if (!checkNull()) {
			player.stop();
			player = null;
		}
		
		ObbExpansionsManager oeManager = ObbExpansionsManager.getInstance();
//		Log.w("DEBUG", "The file root: "+ oeManager.getMainRoot());
		if(oeManager == null)
			 return;
				
		//Set the file and trackname, based on the fileNr
		switch (fileNr) {
		case 1:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
					oeManager.getMainRoot() + "/1.mp3")));
			trackName = "1: Op Weg ";
			break;

		case 2:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
					oeManager.getMainRoot() + "/2.mp3")));
			trackName = "2: Hart van Geuzenveld ";
			break;
		case 3:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/3.mp3")));
			trackName = "3: Leven in een Bouwput ";
			break;
		case 4:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/4.mp3")));
			trackName = "4: De eerste jaren ";
			break;
		case 5:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/5.mp3")));
			trackName = "5: Smeltkroes ";
			break;
		case 6:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/6.mp3")));
			trackName = "6: Verzet tegen sloop ";
			break;
		case 7:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/7.mp3")));
			trackName = "7: Nieuwe Tijden ";
			break;
		case 8:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/8.mp3")));
			trackName = "8: Voetbaldromen ";
			break;
		case 9:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/9.mp3")));
			trackName = "9: Roomse inslag ";
			break;
		case 10:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/10.mp3")));
			trackName = "10: Leren wonen ";
			break;
		case 11:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/11.mp3")));
			trackName = "11: Geuzennaam ";
			break;
		case 12:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/12.mp3")));
			trackName = "12: Kunstgreep ";
			break;
		case 13:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/13.mp3")));
			trackName = "13: Bakkie troost ";
			break;
		case 14:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/14.mp3")));
			trackName = "14: Hoog niveau ";
			break;
		case 15:
			player = MediaPlayer.create(this, Uri.fromFile(new File(
			        oeManager.getMainRoot() + "/15.mp3")));
			trackName = "15: Naar huis ";
			break;

		}

	}

	// Returns the current track name
	public String getCurrentTrackName() {
		return trackName;
	}

	// Checks if the audio is playing or not
	public boolean checkPlaying() {
		if (player == null)
			return false;
		return player.isPlaying();
	}

	// Checks if the player is null or not
	public boolean checkNull() {
		return (player == null ? true : false);
	}

	// Get the position of the media player
	public int getPos() {
		if (player == null)
			return 0;
		return player.getCurrentPosition();
	}

	// Set the position of the media player
	public void setPos(int pos) {
		if (player == null)
			return;
		player.seekTo(pos);
	}

	// Get the duration of the current track
	public int getDur() {
		if (player == null)
			return 0;
		return player.getDuration();
	}

}
