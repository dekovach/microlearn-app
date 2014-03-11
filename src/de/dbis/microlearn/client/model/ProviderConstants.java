package de.dbis.microlearn.client.model;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ProviderConstants implements BaseColumns {
	public static final String AUTHORITY = "de.dbis.microlearn.Content";
	
	// This class cannot be instantiated
	private ProviderConstants() {}
		public static final Uri NOTES_URI = Uri.parse("content://" + AUTHORITY + "/notes");
		public static final String CONTENT_NOTE_TYPE = "vnd.android.cursor.dir/vnd.microlearn.notes";
		public static final String CONTENT_NOTE_ITEM_TYPE = "vnd.android.cursor.item/vnd.microlearn.note";
		public static final String DEFAULT_SORT_ORDER = "note_id DESC";
		public static final String _ID = "_id";
		public static final String NOTE_ID = "note_id";
		public static final String NOTE_NAME = "title";
}