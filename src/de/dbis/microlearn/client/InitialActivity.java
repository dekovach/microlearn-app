package de.dbis.microlearn.client;

import de.dbis.microlearn.client.auth.AuthActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class InitialActivity extends Activity {
	
	private static final String TAG = "InitialActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent intent;
//	    SharedPreferences settings = this.getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
//	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	    User user = User.getInstance();
	    boolean logged = user.isUserLogged();
	    Log.i(TAG, "logged: " + logged);
	    Log.i(TAG,"User: " + User.getInstance().getUsername());
	    
	    if (logged) {
	       intent = new Intent(this, BaseDrawerActivity.class);
	    } else {
	       intent = new Intent(this, WelcomeActivity.class);
	    }
	    startActivity(intent);
	    finish();
	    // note we never called setContentView()
	}

}
