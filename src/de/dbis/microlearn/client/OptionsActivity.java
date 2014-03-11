package de.dbis.microlearn.client;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class OptionsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
		// Get the custom preference

	}
}