package edu.vu.augmented.reality;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class Options extends Activity {
	AudioManager audioManager;
	CheckBox demo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_options);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		SeekBar volControl = (SeekBar) findViewById(R.options.volbar);
		volControl.setMax(maxVolume);
		volControl.setProgress(curVolume);

		volControl
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
								progress, 0);
					}
				});

		demo = (CheckBox) findViewById(R.id.cb_options);
		final SharedPreferences mPrefs = getApplicationContext()
				.getSharedPreferences("Options", MODE_PRIVATE);
		
		if (mPrefs.getBoolean("isChecked", false)) {
			demo.setChecked(true);
		} else {
			demo.setChecked(false);
		}

		demo.setOnClickListener(new View.OnClickListener() {

			SharedPreferences.Editor edit = mPrefs.edit();

			@Override
			public void onClick(View v) {
				if (demo.isChecked()) {
					edit.putBoolean("Demo", false);
					demo.setChecked(true);
					edit.putBoolean("isChecked", true);
				} else {
					edit.putBoolean("Demo", true);
					demo.setChecked(false);
					edit.putBoolean("isChecked", false);
				}
				edit.apply();

			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}