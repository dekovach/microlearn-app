package de.dbis.microlearn.client;

import de.dbis.microlearn.client.model.DBAdapter;
import de.dbis.microlearn.client.model.DBConstants;
import de.dbis.microlearn.client.model.DatabaseHelper;
import de.dbis.microlearn.client.model.ProviderConstants;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

public class NotesListFragment extends MicrolearnFragment 
		implements LoaderManager.LoaderCallbacks<Cursor> {
		
	public static final String ARG_NOTES = "notes_args";
	
	private ListView lvNotes;
//	private DBAdapter dba; 
	private SimpleCursorAdapter notesAdapter;
//	private DatabaseHelper db = null;
//	private SQLiteCursorLoader loader = null;
	private CursorLoader mCursorLoader = null;
	
	public NotesListFragment() {
		// Empty constructor required for fragment subclasses
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
                .penaltyLog()
                .build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
		        .detectLeakedClosableObjects()
		        .penaltyLog()
		        .penaltyDeath()
		        .build());
    	
        View rootView = inflater.inflate(R.layout.main_list, container, false);
        //  int i = getArguments().getInt(ARG_NOTES_NUMBER);
        
        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        // setEmptyText("No phone numbers");
        
        // Create an empty adapter we will use to display the loaded data.
        
//        db = DatabaseHelper.getInstance(getActivity());
        
        notesAdapter = new SimpleCursorAdapter(
        		getActivity(), 
        		android.R.layout.simple_list_item_1, 
        		null, 
        		new String[]{"title"}, 
        		new int[]{android.R.id.text1}, 1);

        lvNotes = (ListView) rootView.findViewById(android.R.id.list);
        lvNotes.setOnItemClickListener(lvNotesOnItemClickListener);
        
        lvNotes.setAdapter(notesAdapter);
 
        // Start out with a progress indicator.
        // setListShown(false);
        
        getLoaderManager().initLoader(0, null, this);
		
//        getActivity().setTitle(title);
        return rootView;
    }
    
//    @Override
//    public void onPause() {
//    	super.onPause();
//    	
//    	db.close();
//    }
//    
//    @Override
//    public void onResume() {
//    	super.onResume();
//    	
//    	db = new DatabaseHelper(getActivity());
//    }
    
//    @Override
//    public void onDestroy() {
//      super.onDestroy();
//
//      db.close();
//    }
    
	OnItemClickListener lvNotesOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView l, View v, int position, long id) {
			Intent i = new Intent(getActivity(), NoteViewCollectionActivity.class);
	    	i.putExtra(NoteViewFragment.ARG_NOTE_CURSOR_ID, (int) position);
	    	i.putExtra(NoteViewFragment.ARG_NOTE_ID, (int) id);
	    	startActivity(i);
		}
	};
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
//        Cursor notes = null;
        
//        dba = new DBAdapter(getActivity());
//        dba.open();
//		notes = dba.getAllNotes();
//		dba.close();
		
//		loader = new SQLiteCursorLoader(getActivity(), db, DBConstants.QUERY_GET_ALL_NOTES, null);
		
		String[] projection = { ProviderConstants._ID, ProviderConstants.NOTE_ID, ProviderConstants.NOTE_NAME };
		mCursorLoader = new CursorLoader(getActivity(), ProviderConstants.NOTES_URI, projection, null, null, null);
		
		return mCursorLoader;
	}
	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    this.mCursorLoader = (CursorLoader) loader;
	    notesAdapter.changeCursor(cursor);
	}
	
	public void onLoaderReset(Loader<Cursor> loader) {
		notesAdapter.changeCursor(null);
	}
	
    /**
     * Updates the content in the adapter.
     */
    public void updateAdapter() {
//    	ContentValues values = new ContentValues(); 
//    	values.put("note_title", "Title of Note 4.4_" + Math.random());
//    	String whereClause = "note_id = ?";
//    	String[] whereArgs = new String[] { "4" };
//    		
//    	this.loader.update(DBConstants.NOTES_TABLE, values, whereClause, whereArgs);
//        dba = new DBAdapter(getActivity());
//        dba.open();
//    	Cursor notes = dba.getAllNotes();
//    	((SimpleCursorAdapter) notesAdapter).changeCursor(notes);
//		((SimpleCursorAdapter) lvNotes.getAdapter()).notifyDataSetChanged();
//		dba.close();
    }
	

}
