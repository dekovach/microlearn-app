package de.dbis.microlearn.client;

import de.dbis.microlearn.client.auth.GoogleLoginActivity;
import de.dbis.microlearn.client.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class WelcomeActivity extends Activity {

	private Button btnGoogleLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);

		btnGoogleLogin = (Button) findViewById(R.id.login_button);
		btnGoogleLogin.setOnClickListener(googleLoginListener);


	}
	
	OnClickListener googleLoginListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(WelcomeActivity.this, GoogleLoginActivity.class);
			startActivity(i);
		}
	};


}
