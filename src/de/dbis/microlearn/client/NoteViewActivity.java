package de.dbis.microlearn.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.dbis.microlearn.client.model.DBAdapter;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class NoteViewActivity extends BaseActivity {
	private static final String TAG = "NoteViewActivity";
	private DBAdapter dba;
	TextView tvTitle;
	EditText etTags;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view);
        
        int noteId = getIntent().getExtras().getInt("noteId");
        
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new InsideWebViewClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginsEnabled(true);
        WebView.enablePlatformNotifications();

        //get clips from the content provider
        Uri aNote = Uri.parse("content://de.dbis.microlearn.Notes/notes/" + noteId);
        String htmlContent = "";
        Cursor cur = managedQuery(aNote, null, null, null, null);
        cur.moveToFirst();
        
        String title = cur.getString(cur.getColumnIndex("note_title"));
        tvTitle = (TextView) findViewById(R.id.noteTitle);
        tvTitle.setText(title);
      
        dba = new DBAdapter(this);
        dba.open();
//        String tags = cur.getString(cur.getColumnIndex("clip_tags"));
        etTags = (EditText) findViewById(R.id.noteTags);
        Cursor tags = dba.getTagForNote(noteId);
        String tagsConcat = "";
        while (tags.moveToNext()) {
        	tagsConcat += tags.getString(tags.getColumnIndex("tag_name")) + ", ";
        }
        etTags.setText(tagsConcat);
        dba.close();
        
        htmlContent = cur.getString(cur.getColumnIndex("html_content"));
  
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";

        
        try {
			webView.loadData(URLEncoder.encode(header + htmlContent,"utf-8").replaceAll("\\+"," "), "text/html", "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        webView.loadUrl("http://gentileschi.informatik.rwth-aachen.de:8080/Microlearn-server/rest/clips/id/9");
//        String summary = "<iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"http://www.youtube.com/embed/bIPcobKMB94\" frameborder=\"0\">";
//        webView.loadData(summary, "text/html", "utf-8");

        
    }
    
    /* Class that prevents opening the Browser */
    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //if URL comes from our server load in same web view otherwise open browser
//        	if(url.toLowerCase().contains("gentileschi"))
//            {
	        	view.loadUrl(url);
	            return true;
//            } 
//        	else{
//            	Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
//                view.getContext().startActivity(intent);
//                return true;
//            }
//        	return false;
        	
        }
    }
}
