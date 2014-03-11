package de.dbis.microlearn.client;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Microlearn extends Application {

	private static Context context;
	
	@Override
	public void onCreate() {
	    super.onCreate();
	    
	    Microlearn.context = getApplicationContext();
	}
	
	public static Context getAppContext(){
		return context;
	}

}
