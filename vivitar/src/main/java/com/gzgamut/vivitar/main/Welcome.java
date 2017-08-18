package com.gzgamut.vivitar.main;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.global.Global;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends Activity {
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		sharedPreferences = getSharedPreferences(
				SettingActivity.SAVE_UNIT_NAME, SettingActivity.MODE_PRIVATE);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (sharedPreferences.getString(Global.APP_FRIST, "").equals(
						Global.APP_ENTERED)) {
					Intent intent = new Intent(Welcome.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(Welcome.this,
							SettingActivity.class);
					startActivity(intent);
					finish();
				}

			}

		}, 2000);
	}

}
