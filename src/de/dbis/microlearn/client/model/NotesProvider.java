package de.dbis.microlearn.client.model;

import static de.dbis.microlearn.client.model.ProviderConstants.*;
import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NotesProvider extends ContentProvider {

	private static HashMap<String, String> sNotesProjectionMap;
	private static final int NOTES = 1;
	private static final int NOTE_BYID = 2;
	private static final int NOTES_BYTAG_ID = 3;
	private static final UriMatcher sUriMatcher;
	
	private DBAdapter dbAdapter;
	private SQLiteDatabase db;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, "notes", NOTES);
		sUriMatcher.addURI(AUTHORITY, "notes/#", NOTE_BYID);
		sUriMatcher.addURI(AUTHORITY, "notes/tag/#", NOTES_BYTAG_ID);
		
		sNotesProjectionMap = new HashMap<String, String>();
		sNotesProjectionMap.put(NOTE_ID, "Notes.note_id as _id");
		sNotesProjectionMap.put(NOTE_NAME, "Notes.note_title as name");
	}
	
	@Override
	   public String getType(Uri uri) {
	      switch (sUriMatcher.match(uri)){
	         //---get all books---
	         case NOTES:
	            return CONTENT_NOTE_TYPE;
	         //---get a particular book---
	         case NOTE_BYID:                
	            return CONTENT_NOTE_ITEM_TYPE;
	         default:
	            throw new IllegalArgumentException("Unsupported URI: " + uri);        
	      }   
	   }
	
	@Override
	   public boolean onCreate() {
	      Context context = getContext();
	      DBAdapter dbAdapter = new DBAdapter(context);
	      db = dbAdapter.open();
	      return (db == null)? false:true;
	   }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	    sqlBuilder.setTables(DBConstants.NOTES_TABLE);
		
		if (sUriMatcher.match(uri) == NOTE_BYID)
	         //---if getting a particular note---
	         sqlBuilder.appendWhere(
	            NOTE_BYID + " = " + uri.getPathSegments().get(1));                
	       
//	      if (sortOrder==null || sortOrder=="")
//	         sortOrder = TITLE;
		
		Cursor c = sqlBuilder.query(
		         db, 
		         projection, 
		         selection, 
		         selectionArgs, 
		         null, 
		         null, 
		         sortOrder);
		   
		      //---register to watch a content URI for changes---
		      c.setNotificationUri(getContext().getContentResolver(), uri);
		      return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
