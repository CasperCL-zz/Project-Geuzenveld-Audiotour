package com.hetfotogeniekegeluid.activity;

import com.hetfotogeniekegeluid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/** This is where is all starts.
 * 
 * @author Casper
 *
 */
public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Thread timer = new Thread(){
			
			@Override
			public void run(){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(mainActivity);
			}
		};
		timer.start();
	}
}
