package de.dbis.microlearn.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.dbis.microlearn.client.model.DBAdapter;
import de.dbis.microlearn.client.model.DatabaseHelper;
import de.dbis.microlearn.client.model.Note;
import de.dbis.microlearn.client.model.ProviderConstants;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

public class NoteViewFragment extends Fragment {
		
		public static final String ARG_NOTE_CURSOR_ID = "noteview_note_cursor_id";
		public static final String ARG_NOTE_ID = "noteview_note_id";
		public static final String ARG_NOTE_TITLE = "noteview_note_title";
		public static final String ARG_NOTE_CONTENT = "noteview_note_content";
		
		private DBAdapter dba;
		private DatabaseHelper db;
		TextView tvTitle;
		EditText etTags;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.content_view, container, false);
			
			Bundle args = getArguments();
			int noteId = args.getInt(ARG_NOTE_ID);
			
			//query database
			db = DatabaseHelper.getInstance(getActivity());
			Note note = db.fetchNote(noteId);
			
			String htmlContent = note.getClipsAsHtml();
			String noteTitle = note.getNoteTitle();
			String tags = note.getTagsAsString();
			
	        WebView webView = (WebView) rootView.findViewById(R.id.webView1);
	        webView.getSettings().setJavaScriptEnabled(true);
	//        webView.setWebViewClient(new InsideWebViewClient());
	        webView.getSettings().setAllowFileAccess(true);
	        webView.getSettings().setPluginsEnabled(true);
	        WebView.enablePlatformNotifications();
	
	        tvTitle = (TextView) rootView.findViewById(R.id.noteTitle);
	        tvTitle.setText(noteTitle);
	        etTags = (EditText) rootView.findViewById(R.id.noteTags);
	        etTags.setText(tags);
	      
	        //load tags for this note
//	        dba = new DBAdapter(getActivity());
//	        dba.open();
//	//        String tags = cur.getString(cur.getColumnIndex("clip_tags"));
//	        etTags = (EditText) rootView.findViewById(R.id.noteTags);
//	        Cursor tags = dba.getTagForNote(noteId);
//	        String tagsConcat = "";
//	        while (tags.moveToNext()) {
//	        	tagsConcat += tags.getString(tags.getColumnIndex("tag_name")) + ", ";
//	        }
//	        etTags.setText(tagsConcat);
//	        dba.close();
	        
//	        htmlContent = cur.getString(cur.getColumnIndex("html_content"));
	        
			webView.loadData(htmlContent, "text/html", "utf-8");
			
			return rootView;
		}
		
//		public Note fetchNote(int noteId) {
//			Cursor cursor = getContentResolver().query(ProviderConstants.NOTES_URI, )
//		}
}