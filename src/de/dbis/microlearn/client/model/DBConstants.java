package de.dbis.microlearn.client.model;

public class DBConstants {
	  public static final String DB_NAME = "microlearn.db";
	  public static final int DATABASE_VERSION = 6;
	  
	  public static final String NOTEBOOKS_TABLE= "Notebooks";
	  public static final String NOTES_TABLE= "Notes";
	  public static final String CLIPS_TABLE= "Clips";
	  public static final String RESOURCES_TABLE= "Resources";
	  public static final String TAGS_TABLE= "Tags";
	  public static final String TAGLIST_TABLE= "Taglist";
	  
	  public static final String QUERY_GET_ALL_NOTES = "SELECT note_id AS _id, title " +
												 "FROM " + DBConstants.NOTES_TABLE + " " +
											  	 "ORDER BY note_id ASC";
}