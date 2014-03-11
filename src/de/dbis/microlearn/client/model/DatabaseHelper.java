package de.dbis.microlearn.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DatabaseHelper";

	private static DatabaseHelper sInstance = null;
	
	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}
	
	private DatabaseHelper(Context context) {
	    super(context, DBConstants.DB_NAME, null, DBConstants.DATABASE_VERSION);
	  }
	
	public void closeInstance() {
		if (sInstance != null) {
			sInstance.close();
		}
	}
	 
	  @Override
	  public void onCreate(SQLiteDatabase db) {
		  db.execSQL("CREATE TABLE 'Notebooks' " +
		  		"('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
		  		"'notebook_id' INTEGER UNIQUE , " +
		  		"'name' VARCHAR NOT NULL , " +
		  		"'created' DATETIME NOT NULL , " +
		  		"'updated' DATETIME, " +
		  		"'deleted' DATETIME, " +
		  		"'dirty' INTEGER NOT NULL, " +
		  		"'usn' INTEGER NOT NULL  DEFAULT 1 )");
		  
		  db.execSQL("CREATE TABLE 'Notes' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'note_id' INTEGER UNIQUE , 'notebook_id' INTEGER, 'title' TEXT, 'rating' INTEGER, 'question' BOOL, 'improve' BOOL, 'created' DATETIME, 'updated' DATETIME, 'deleted' DATETIME, 'dirty' INTEGER NOT NULL  DEFAULT 0, 'usn' INTEGER NOT NULL  DEFAULT 1)");
		  
		  db.execSQL("CREATE TABLE 'Clips' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'clip_id' INTEGER UNIQUE , 'note_id' INTEGER NOT NULL , 'sort_order' INTEGER, 'content' TEXT NOT NULL , 'source_url' VARCHAR, 'is_question' BOOL DEFAULT 0, 'created' DATETIME, 'updated' DATETIME, 'deleted' DATETIME, 'dirty' DATETIME NOT NULL  DEFAULT 0, 'usn' INTEGER NOT NULL  DEFAULT 1)");
		  
		  db.execSQL("CREATE TABLE 'Resources' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'resource_id' INTEGER UNIQUE , 'clip_id' INTEGER, 'data' VARCHAR, 'mime' VARCHAR, 'width' INTEGER, 'height' INTEGER, 'created' DATETIME, 'updated' DATETIME, 'deleted' DATETIME, 'dirty' INTEGER DEFAULT 0, 'usn' INTEGER NOT NULL  DEFAULT 1)");
		  
		  db.execSQL("CREATE TABLE 'Tags' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'tag_id' INTEGER UNIQUE , 'name' VARCHAR NOT NULL , 'created' DATETIME, 'updated' DATETIME, 'deleted' DATETIME, 'dirty' INTEGER DEFAULT 0, 'usn' INTEGER NOT NULL  DEFAULT 1)");
		  
		  db.execSQL("CREATE TABLE 'Taglist' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'taglist_id' INTEGER, 'tag_id' INTEGER NOT NULL , 'note_id' INTEGER NOT NULL , 'sort_order' INTEGER NOT NULL  DEFAULT 0, 'created' DATETIME, 'updated' DATETIME, 'deleted' DATETIME, 'dirty' INTEGER DEFAULT 0, 'usn' INTEGER NOT NULL  DEFAULT 1)");
		  
//		  db.execSQL("CREATE TABLE " + DBConstants.NOTES_TABLE + " " +
//				  "(note_id INTEGER PRIMARY KEY, " +
//				  "note_title TEXT, " +
//		  "html_content TEXT)");
//		  
//		  db.execSQL("CREATE TABLE " + DBConstants.TAGLIST_TABLE + " " +
//				  "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//				  "note_id INTEGER, " +
//		  		  "tag_name TEXT, " +
//		  		  "UNIQUE(note_id,tag_name))");
		  
		  initializeWithTestData(db);
		  
		  
		  Log.i(TAG, "DB created!");
	  }
	  
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  Log.w(TAG, 
	              "Upgrading database from version " + 
	              oldVersion + " to " + newVersion + 
	              ", which will destroy all old data");
	         db.execSQL("DROP TABLE IF EXISTS " + DBConstants.CLIPS_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + DBConstants.NOTES_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + DBConstants.NOTEBOOKS_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + DBConstants.RESOURCES_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TAGS_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TAGLIST_TABLE);
	         onCreate(db);
	  }
	  
	  private void initializeWithTestData(SQLiteDatabase db) {
//		  db.execSQL("INSERT INTO " + DBConstants.NOTES_TABLE +  " " +
//				" VALUES(1,\"Title of Note 1\"," +
//		  		"\"<h1>HTML CONTENT</h1><br/><iframe class=\'youtube-player\' " +
//		  		"type=\'text/html\' width=\'640\' height=\'385\' " +
//		  		"src=\'http://www.youtube.com/embed/bIPcobKMB94\' frameborder=\'0\'>\")");
		  
		  db.execSQL("INSERT INTO Clips VALUES(1,1,1,1,'<img width=\"380\" height=\"385\" class=\"thumbimage ml-selected\" src=\"http://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Chemische_Struktur_der_DNA.svg/330px-Chemische_Struktur_der_DNA.svg.png\" alt=\"\" ml_sel_num=\"<img width=\"380\" height=\"385\" class=\"thumbimage ml-selected\" src=\"http://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Chemische_Struktur_der_DNA.svg/330px-Chemische_Struktur_der_DNA.svg.png\" alt=\"\" ml_sel_num=\"4\" />','http://de.wikipedia.org/wiki/Desoxyribonukleins%C3%A4ure',1,'2012-09-19 11:41:57',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Clips VALUES(2,2,1,2,'<span id=\"Die_prokaryotische_Zelle\" class=\"mw-headline ml-selected\" ml_sel_num=\"3\" width=\"380\">Die prokaryotische Zelle</span>','http://de.wikipedia.org/wiki/Zelle_%28Biologie%29',0,'2012-09-19 12:30:26',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Clips VALUES(3,3,1,0,'<img width=\"380\" height=\"521\" class=\"thumbimage ml-selected\" src=\"http://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/DNA_simple.svg/220px-DNA_simple.svg.png\" alt=\"\" ml_sel_num=\"3\" />','http://de.wikipedia.org/wiki/Desoxyribonukleins%C3%A4ure',0,'2012-09-19 12:07:42',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Clips VALUES(4,5,2,NULL,'<span id=\"ID1\" class=\"ml-selected\" ml_sel_num=\"1\" width=\"380\">Der erste Heading</span>','http://manet.informatik.rwth-aachen.de/~kovachev/blank/',1,'2012-10-24 16:07:04',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Clips VALUES(5,6,2,0,'<span id=\"ID1\" class=\"ml-selected\" ml_sel_num=\"1\" width=\"380\">Der zweite Heading</span>','http://manet.informatik.rwth-aachen.de/~kovachev/blank/',1,'2012-10-24 16:07:04',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Notebooks VALUES(1,1,'Dejan''s Notebook','2012-09-19 16:02:01',NULL,NULL,'',1)");
		  db.execSQL("INSERT INTO Notes (_id, note_id, title, rating, question, improve, created, updated, deleted, dirty, notebook_id, usn) VALUES(1,1,'Die Zelle',NULL,NULL,NULL,'2012-09-19 16:02:01',NULL,NULL,0,1,1)");
		  db.execSQL("INSERT INTO Notes (_id, note_id, title, rating, question, improve, created, updated, deleted, dirty, notebook_id, usn) VALUES(2,2,'Blank Test',NULL,NULL,NULL,'2012-09-19 18:02:01',NULL,NULL,0,1,1)");
		  db.execSQL("INSERT INTO Taglist VALUES(1,1,1,1,0,'2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Taglist VALUES(2,2,1,2,0,'2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Taglist VALUES(3,3,2,2,0,'2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Taglist VALUES(4,4,4,1,0,'2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Taglist VALUES(5,5,27,1,0,'2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Tags VALUES(1,1,'tag1','2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Tags VALUES(2,2,'tag2','2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Tags VALUES(3,4,'tag4','2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Tags VALUES(4,3,'tag3','2012-09-19 16:02:01',NULL,NULL,0,1)");
		  db.execSQL("INSERT INTO Tags VALUES(5,27,'tag27','2012-09-19 16:02:01',NULL,NULL,0,1)");
		  
	  }
	  
	  public Note fetchNote(int noteId) {
		  Note note = new Note();
		  note.setNoteId(noteId);
		  
		  List<String> clips = new ArrayList<String>();
		  List<String> tags = new ArrayList<String>();
		  
		  SQLiteDatabase dbReadable = this.getReadableDatabase();

		  String selectNoteQuery = "SELECT _id, note_id, title FROM Notes WHERE note_id=?";
		  String[] params = new String[] {String.valueOf(noteId)};
		  Cursor noteCursor = dbReadable.rawQuery(selectNoteQuery, params);
		  if (noteCursor.moveToFirst()) {
			  note.setTitle(noteCursor.getString(noteCursor.getColumnIndex("title")));
		  }
		  noteCursor.close();
		  		  
		  String selectClipsQuery = "SELECT _id, clip_id, content, sort_order, source_url, is_question FROM Clips WHERE note_id=? ORDER BY sort_order ASC";
		  Cursor clipsCursor = dbReadable.rawQuery(selectClipsQuery, params);
		  while (clipsCursor.moveToNext()){
			  clips.add(clipsCursor.getString(clipsCursor.getColumnIndex("content")));
		  }
		  note.setClips(clips);
		  clipsCursor.close();
		  
		  String selectTagsQuery = "SELECT Tags._id, Tags.tag_id, Tags.name, Taglist.sort_order FROM Taglist JOIN Tags ON Taglist.tag_id = Tags.tag_id WHERE Taglist.note_id=? ORDER BY sort_order ASC";
		  Cursor tagsCursor = dbReadable.rawQuery(selectTagsQuery, params);
		  while (tagsCursor.moveToNext()){
			  tags.add(tagsCursor.getString(tagsCursor.getColumnIndex("name")));
		  }
		  note.setTags(tags);
		  tagsCursor.close();
		  
		  dbReadable.close();
		  
		  return note;
	  }
	  
	  public HashMap<String,Integer> fetchCounts() {

		  HashMap<String,Integer> counts = new HashMap<String,Integer>();
		  SQLiteDatabase dbReadable = this.getReadableDatabase();

		  String countQuery = "SELECT COUNT(*) FROM Notebooks UNION ALL SELECT COUNT(*) FROM Notes UNION ALL SELECT COUNT(*) FROM Tags";
		  Cursor countCursor = dbReadable.rawQuery(countQuery, null);
		  if (countCursor.moveToFirst()) {
			  counts.put("notebooks", Integer.valueOf(countCursor.getInt(0)));
		  }
		  if (countCursor.moveToNext()) {
			  counts.put("notes", Integer.valueOf(countCursor.getInt(0)));
		  }
		  if (countCursor.moveToNext()) {
			  counts.put("tags", Integer.valueOf(countCursor.getInt(0)));
		  }

		  countCursor.close();
		  dbReadable.close();
		  
		  return counts;
	  }
  }