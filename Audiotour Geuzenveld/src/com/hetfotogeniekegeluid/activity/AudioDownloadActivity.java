package com.hetfotogeniekegeluid.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.hetfotogeniekegeluid.R;
import com.hetfotogeniekegeluid.service.AudioDownloadService;

public class AudioDownloadActivity extends Activity implements IDownloaderClient  {

	private IStub mDownloaderClientStub;
	private IDownloaderService mRemoteService;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        // Inflate layout that shows download progress
        setContentView(R.layout.activity_download);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        
        downloadAudio();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		if (null != mDownloaderClientStub) {
	        mDownloaderClientStub.disconnect(this);
	    }
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		if (null != mDownloaderClientStub) {
	        mDownloaderClientStub.connect(this);
	    }
		super.onResume();
	}
	
	public void downloadAudio(){
    	Log.w("DOWNLOAD", "Started downloading");
		// Build an Intent to start this activity from the Notification
        Intent notifierIntent = new Intent(this, AudioDownloadActivity.class);
        notifierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notifierIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Start the download service (if required)
        int startResult = -1;
		try {
			startResult = DownloaderClientMarshaller.startDownloadServiceIfRequired(this,
			                pendingIntent, AudioDownloadService.class);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

    	Log.w("DOWNLOAD", "Start result: " + startResult);
        // If download has started, initialize this activity to show download progress
        if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
        	Log.w("DOWNLOAD", "Initiating download");
            // This is where you do set up to display the download progress (next step)
        	// Instantiate a member instance of IStub
        	mDownloaderClientStub = DownloaderClientMarshaller.CreateStub(this,
                    AudioDownloadService.class);
        	return;
        }
	}

	// IDownloaderClient methods
	@Override
	public void onServiceConnected(Messenger m) {
		mRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
	    mRemoteService.onClientUpdated(mDownloaderClientStub.getMessenger());

		Log.w("PROGRESS", "Remote service connected");
	}

	@Override
	public void onDownloadStateChanged(int newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloadProgress(DownloadProgressInfo progress) {
		long progressL = progress.mOverallProgress;
		Log.w("PROGRESS", "Progess: " + progressL);

		progressBar.setMax((int) progress.mOverallTotal);
		progressBar.setProgress((int)progressL);
	}
}
