package com.hetfotogeniekegeluid.activity;

import com.hetfotogeniekegeluid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	public void onStartClick(View v) {
		startActivity(new Intent(MenuActivity.this, MapActivity.class));
	}

	public void onInstructionClick(View v) {
		startActivity(new Intent(MenuActivity.this, InstructionMenuActivity.class));
	}
	
	public void onColofonClick(View v) {
		startActivity(new Intent(MenuActivity.this, MapActivity.class));
	}

}
