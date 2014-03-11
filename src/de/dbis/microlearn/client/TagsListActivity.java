package de.dbis.microlearn.client;

import de.dbis.microlearn.client.model.DBAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TagsListActivity extends ListActivity {
	private DBAdapter dba; 
	Cursor tags = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        setContentView(R.layout.main);
        
        
        dba = new DBAdapter(this);
        dba.open();
        
        tags = dba.getDistinctTags();
        
        
        ListAdapter tagsAdapter = new SimpleCursorAdapter(
        		this, 
        		android.R.layout.simple_list_item_1, 
        		tags, 
        		new String[]{"tag_name"}, 
        		new int[]{android.R.id.text1});
		setListAdapter(tagsAdapter);
		
		dba.close();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		tags.moveToPosition(position);
		String tagName = tags.getString(tags.getColumnIndex("tag_name"));

		
		Intent i = new Intent(this, NoteListActivity.class);
    	i.putExtra("tagName", tagName);
    	i.putExtra("view", Constants.LIST_BY_TAGS);
    	startActivity(i);
	}
}
