package de.dbis.microlearn.client;

import java.util.HashMap;

import de.dbis.microlearn.client.model.DatabaseHelper;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseDrawerActivity extends BaseActivity {

	protected DrawerLayout mFullLayout;
	protected FrameLayout mContentLayout;
	protected LinearLayout mLeftDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
    private TextView mDrawerUsername;
	
	private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
	private DatabaseHelper db;
	
	/**
	 * Add navigation drawer functionality to every activity that instantiates this class
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		mFullLayout = (android.support.v4.widget.DrawerLayout) getLayoutInflater().inflate(R.layout.navigation_drawer, null); 
//		mContentLayout = (FrameLayout) mFullLayout.findViewById(R.id.content_frame);
//		getLayoutInflater().inflate(layoutResID, mContentLayout, true);
//		super.setContentView(mFullLayout);
		
		super.setContentView(R.layout.navigation_drawer);
		
		mFullLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mFullLayout.openDrawer(mLeftDrawer);
		
		// display the username
		mDrawerUsername = (TextView) findViewById(R.id.drawer_display_user_account);
		String username = User.getInstance().getUsername();
		mDrawerUsername.setText(username);
		
		// set a custom shadow that overlays the main content when the drawer opens
        mFullLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		findViewById(R.id.nav_drawer_notes).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectContentView(Constants.SHOW_NOTES);
			}
		});
		
		
		mTitle = mDrawerTitle = getResources().getString(R.string.app_name);

		mDrawerToggle = new ActionBarDrawerToggle(
				this, 
				mFullLayout,
                R.drawable.ic_drawer, 
                R.string.drawer_open, 
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mFullLayout.setDrawerListener(mDrawerToggle);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            selectContentView(Constants.SHOW_NOTES);
        }
        
        setCounts();
	}
	
    protected void selectContentView(int showNotes) {
    	Fragment fragment = null;
    	String title = null;
    	switch (showNotes) {
		case Constants.SHOW_NOTEBOOKS:
			
			break;
		case Constants.SHOW_TAGS:
			
			break;
		case Constants.SHOW_NOTES:
		default:
			fragment = new NotesListFragment();
			title = getResources().getString(R.string.label_notes);
			break;
		}
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
					   .replace(R.id.content_frame, fragment)
					   .commit();
		mFullLayout.closeDrawer(mLeftDrawer);
		setTitle(title);
	}

	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mFullLayout.isDrawerOpen(mLeftDrawer);
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
		
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	FragmentManager fragmentManager = getFragmentManager();
	        	((MicrolearnFragment) fragmentManager.findFragmentById(R.id.content_frame)).updateAdapter();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setCounts() {
    	//query database
		db = DatabaseHelper.getInstance(this);
		HashMap<String, Integer> counts = db.fetchCounts();
		Integer notebooksCount = (Integer) counts.get("notebooks");
		Integer notesCount = (Integer) counts.get("notes");
		Integer tagsCount = (Integer) counts.get("tags");
		
		TextView tvNotebooksCount = (TextView) findViewById(R.id.nav_drawer_notebooks_count);
		tvNotebooksCount.setText(notebooksCount.toString());
		TextView tvNotesCount = (TextView) findViewById(R.id.nav_drawer_notes_count);
		tvNotesCount.setText(notesCount.toString());
		TextView tvTagsCount = (TextView) findViewById(R.id.nav_drawer_tags_count);
		tvTagsCount.setText(tagsCount.toString());
    }
	
}