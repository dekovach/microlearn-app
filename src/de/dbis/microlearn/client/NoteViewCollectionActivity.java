package de.dbis.microlearn.client;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

import de.dbis.microlearn.client.model.DBAdapter;
import de.dbis.microlearn.client.model.DBConstants;
import de.dbis.microlearn.client.model.DatabaseHelper;
import de.dbis.microlearn.client.model.MicroContentProvider;
import de.dbis.microlearn.client.model.ProviderConstants;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class NoteViewCollectionActivity extends FragmentActivity
		implements LoaderManager.LoaderCallbacks<Cursor> {
	
//	NoteViewCollectionPagerAdapter mNoteViewCollectionPagerAdapter;
	CursorPagerAdapter mNoteViewCollectionPagerAdapter;
	ViewPager mViewPager;
	int selectedNoteCursorId;
	int selectedNoteId;
	
	private DBAdapter dba; 
	private Cursor notesCursor;
	private DatabaseHelper db = null;
	private SQLiteCursorLoader loader = null;
	private CursorLoader mCursorLoader = null;
	
	public void onCreate(Bundle savedBundleInstance) {
		super.onCreate(savedBundleInstance);
		
		selectedNoteCursorId = getIntent().getExtras().getInt(NoteViewFragment.ARG_NOTE_CURSOR_ID);
		selectedNoteId = getIntent().getExtras().getInt(NoteViewFragment.ARG_NOTE_ID);
		
		StrictMode.setThreadPolicy(
				new StrictMode.ThreadPolicy.Builder().detectAll()
	                .penaltyLog().build());
		StrictMode.setVmPolicy(
				new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
			        .detectLeakedClosableObjects()
			        .penaltyLog()
			        .penaltyDeath()
			        .build());
		
		setContentView(R.layout.activity_collection_noteview);
		
        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
//        mNoteViewCollectionPagerAdapter = 
//        	new NoteViewCollectionPagerAdapter(getSupportFragmentManager(), 
//        	notesCursor);
        mNoteViewCollectionPagerAdapter = 
        	new CursorPagerAdapter<NoteViewFragment>(getSupportFragmentManager(),
        			NoteViewFragment.class, notesCursor);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mNoteViewCollectionPagerAdapter);
        
        db = DatabaseHelper.getInstance(this);
        
        getLoaderManager().initLoader(0, null, this);
	}
	
    @Override
    public void onDestroy() {
      super.onDestroy();

      db.close();
    }
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//		loader = new SQLiteCursorLoader(this, db, 
//				DBConstants.QUERY_GET_ALL_NOTES, null);
		
		String[] projection = { ProviderConstants._ID, ProviderConstants.NOTE_ID, ProviderConstants.NOTE_NAME };
		mCursorLoader = new CursorLoader(this, ProviderConstants.NOTES_URI, projection, null, null, null);
		
		return mCursorLoader;
	}
	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//	    this.loader = (SQLiteCursorLoader) loader;
//	    this.notesCursor = cursor;
//	    mNoteViewCollectionPagerAdapter.swapCursor(cursor);
	    this.mCursorLoader = (CursorLoader) loader;
	    this.notesCursor = cursor;
	    mNoteViewCollectionPagerAdapter.swapCursor(cursor);
        mViewPager.setCurrentItem(selectedNoteCursorId);
	}
	
	public void onLoaderReset(Loader<Cursor> loader) {
		this.notesCursor = null;
	}
	
	
	public class NoteViewCollectionPagerAdapter 
			extends FragmentStatePagerAdapter {

		private Cursor cursor = null;
		
		public NoteViewCollectionPagerAdapter(FragmentManager fm, Cursor cursor) {
			super(fm);
			this.cursor = cursor;
		}

		@Override
		public Fragment getItem(int position) {
			 if (cursor == null) // shouldn't happen
				 return null;
			 
			Fragment fragment = new NoteViewFragment();
			Bundle args = new Bundle();
			cursor.moveToPosition(position);
			int noteId = cursor.getInt(cursor.getColumnIndex("_id"));
			args.putInt(NoteViewFragment.ARG_NOTE_ID, noteId);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}
	}


		
}