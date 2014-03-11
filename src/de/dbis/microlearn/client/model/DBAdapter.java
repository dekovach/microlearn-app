package de.dbis.microlearn.client.model;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.text.format.Time;
import android.util.Log;
 
public class DBAdapter {
 
	private static final String TAG = "DBAdapter";

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
 
	private final Context context;
  
  public DBAdapter(Context context) {
      this.context = context;
      dbHelper = DatabaseHelper.getInstance(context);
  }
 
  public SQLiteDatabase open() throws SQLException {
     db = dbHelper.getWritableDatabase();
     return db;
  }
  
  public void close() 
  {
      dbHelper.close();
  }
  
  public Cursor getAllNotes() {
      return this.db.rawQuery(DBConstants.QUERY_GET_ALL_NOTES, null);
   }
  
  public Cursor getDistinctTags() {
	  return this.db.rawQuery("SELECT _id, tag_name " +
	  		"FROM " + DBConstants.TAGLIST_TABLE +  " " +
	  		" GROUP BY tag_name", null);
  }
  
  public Cursor getNotesByTag(String tagName) {
	  return this.db.rawQuery("SELECT Notes.note_id AS _id, note_title, html_content " +
	  		"FROM " + DBConstants.NOTES_TABLE + " " +
	  		"JOIN Tagmap ON Notes.note_id = Tagmap.note_id " +
	  		"WHERE Tagmap.tag_name = '" + tagName + "' " +
	  		"ORDER BY Notes.note_id ASC", null);
  }
  
  public Cursor getTagForNote(int noteId) {
	  return this.db.rawQuery("SELECT tag_name " +
	  		"FROM " + DBConstants.TAGLIST_TABLE + " " +
	  		"WHERE note_id = '" + noteId + "'", null);
  }
  
  public boolean populateDB(ArrayList<Note> notesArray, ArrayList<Tagmap> tmArray){
	  db.execSQL("DELETE FROM Notes");
	  db.execSQL("DELETE FROM Tagmap");
	  
	// Create a single InsertHelper to handle this set of insertions.
	  InsertHelper ih = new InsertHelper(db, "Notes");
	// Get the numeric indexes for each of the columns that we're updating
      final int idColumnIndex = ih.getColumnIndex("note_id");
      final int noteTitleColumnIndex = ih.getColumnIndex("note_title");
      final int htmlContentColumnIndex = ih.getColumnIndex("html_content");
	  
	  for(Note c:notesArray){
		// Get the InsertHelper ready to insert a single row
          ih.prepareForInsert();
		  
       // Add the data for each column
          ih.bind(idColumnIndex, c.getNoteId());
          ih.bind(noteTitleColumnIndex, c.getNoteTitle());
//          ih.bind(htmlContentColumnIndex, c.getHtmlContent());

          // Insert the row into the database.
          ih.execute();
	  }
	  
	// Create a single InsertHelper to handle this set of insertions.
	  InsertHelper ihTm = new InsertHelper(db, "Tagmap");
	// Get the numeric indexes for each of the columns that we're updating
      final int idNoteColumnIndex = ihTm.getColumnIndex("note_id");
      final int tagnameColumnIndex = ihTm.getColumnIndex("tag_name");
	  
	  for(Tagmap tm:tmArray){
		// Get the InsertHelper ready to insert a single row
          ihTm.prepareForInsert();
		  
       // Add the data for each column
          ihTm.bind(idNoteColumnIndex, tm.getNoteId());
          ihTm.bind(tagnameColumnIndex, tm.getTagName());

          // Insert the row into the database.
          ihTm.execute();
	  }
	  
	  Log.i(TAG, "DB populated!");
	  
	  
	  return true;
  }

public Cursor getNoteTitles() {
	
	return null;
}


}