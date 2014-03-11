package de.dbis.microlearn.client;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public abstract class BaseActivity extends Activity {

	protected android.support.v4.widget.DrawerLayout fullLayout;
	protected FrameLayout contentLayout;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.notes_list_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_settings:
	    		Intent intent = new Intent(this, SettingsActivity.class);
	    		startActivity(intent);
	    		break;
	    	case R.id.action_logout:
	    		User.getInstance().logoutUser();
	    		intent = new Intent(this, WelcomeActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    		startActivity(intent);
	    		this.finish();
	    		break;
	        default:
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	
}