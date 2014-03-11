package de.dbis.microlearn.client;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import de.dbis.microlearn.client.model.DBAdapter;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

//import com.markupartist.android.widget.PullToRefreshListView;
//import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class NoteListActivity extends BaseDrawerActivity 
		implements PullToRefreshAttacher.OnRefreshListener{
	
	private ListView lvNotes;
	private DBAdapter dba; 
	private SimpleCursorAdapter notesAdapter;
    private PullToRefreshAttacher mPullToRefreshAttacher;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        
        lvNotes = (ListView) findViewById(android.R.id.list);
        lvNotes.setOnItemClickListener(lvNotesOnItemClickListener);
        
        int viewType = Constants.LIST_BY_DEFAULT;
        Bundle extras =  getIntent().getExtras();
        if (extras != null && extras.containsKey("view")) {
        	viewType = getIntent().getExtras().getInt("view");
        }
        Cursor notes = null;
        
        dba = new DBAdapter(this);
        dba.open();
        
        switch (viewType) {
			case Constants.LIST_BY_LATEST:
				notes = dba.getAllNotes();
				break;
			case Constants.LIST_BY_TAGS:
				String tagName = getIntent().getExtras().getString("tagName");
				notes = dba.getNotesByTag(tagName);
				break;
			default:
				notes = dba.getAllNotes();
				break;
		}
        
        notesAdapter = new SimpleCursorAdapter(
        		this, 
        		android.R.layout.simple_list_item_1, 
        		notes, 
        		new String[]{"note_title"}, 
        		new int[]{android.R.id.text1}, 1);
		lvNotes.setAdapter(notesAdapter);
		
		dba.close();
		
        /**
         * Here we create a PullToRefreshAttacher manually without an Options instance.
         * PullToRefreshAttacher will manually create one using default values.
         */
        mPullToRefreshAttacher = PullToRefreshAttacher.get(this);
        
        // Set the Refreshable View to be the ListView and the refresh listener to be this.
        mPullToRefreshAttacher.addRefreshableView(lvNotes, this);
	}
	
	OnItemClickListener lvNotesOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView l, View v, int position, long id) {
			Intent i = new Intent(NoteListActivity.this, NoteViewActivity.class);
	    	i.putExtra("noteId", (int) id);
	    	startActivity(i);
		}

		
		
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	            updateAdapter();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    @Override
    public void onRefreshStarted(View view) {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
//                try {
//                    Thread.sleep(Constants.SIMULATED_REFRESH_LENGTH);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                runOnUiThread(new Runnable() {
                    public void run() {
                    	updateAdapter();
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshAttacher that the refresh has finished
                mPullToRefreshAttacher.setRefreshComplete();
            }
        }.execute();
    }
	
    /**
     * Updates the content in the adapter.
     */
    public void updateAdapter() {
        dba = new DBAdapter(this);
        dba.open();
    	Cursor notes = dba.getAllNotes();
    	((SimpleCursorAdapter) notesAdapter).changeCursor(notes);
		((SimpleCursorAdapter) lvNotes.getAdapter()).notifyDataSetChanged();
		dba.close();
    }

}
