package de.dbis.microlearn.client;

import de.dbis.microlearn.client.auth.AuthActivity;
import de.dbis.microlearn.client.sync.SimpleSyncService;
import de.dbis.microlearn.client.sync.SimpleSyncService.LocalBinder;
import de.dbis.microlearn.client.sync.SyncAdapterService;
import de.dbis.microlearn.client.sync.SyncService;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LauncherActivity extends ListActivity {
	private static final String TAG = "LauncherActivity";
    private boolean mIsBound;
    private SimpleSyncService mBoundService;
    private ProgressDialog pd;
    /** Messenger for communicating with service. */
    Messenger mService = null;


    private String[] listItems = {"Latest", "Tags"};
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        String[] menuItems = getResources().getStringArray(R.array.launcher_menu);

        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuItems));
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	Intent i = new Intent(LauncherActivity.this, SimpleSyncService.class);
    	i.putExtra(SimpleSyncService.EXTRA_MESSENGER, new Messenger(handler));
    	bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
//    	Intent i = new Intent(this, SimpleSyncService.class);
//    	stopService(i);
    	
    	// Detach our existing connection.
        if(mIsBound){
	    	unbindService(mConnection);
	        mIsBound = false;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i;
		switch (position){
		case 0:
			i = new Intent(this, NoteListActivity.class);
			i.putExtra("view", Constants.LIST_BY_LATEST);
			startActivity(i);
			break;
		case 1:
			i = new Intent(this, TagsListActivity.class);
			i.putExtra("view", Constants.LIST_BY_TAGS);
			startActivity(i);
			break;
		default:
			super.onListItemClick(l, v, position, id);
			
		}
    	
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//        case R.id.sync:
            //invoke sync service
//        	invokeSyncService();
        	
//        	Toast t = Toast.makeText(this, "Sync invoked.", Toast.LENGTH_SHORT);
//            t.show();
//            return true;
       case R.id.logout:
        	logout();
        	return true;
       case R.id.options:
    	   Intent optionsActivity = new Intent(getBaseContext(),
		                   OptionsActivity.class);
		   startActivity(optionsActivity);
    	   return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void logout() {
    	//set settings that user is logged in
//    	SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("logged", false);

        // Commit the edits!
        editor.commit();
        Intent i1 = new Intent(this, AuthActivity.class);
        startActivity(i1);
        finish();
    }
    
    private void invokeSyncService(){
//    	Intent i = new Intent(this, SimpleSyncService.class);
//    	startService(i);
    	
    	//check Internet connection
    	if(isOnline()){
    	
	    	Log.i(TAG, "Thread id: " + Thread.currentThread().getId());
	    	
	    	pd = ProgressDialog.show(this, "Syncing...", "", true, false);
	    	
	        if(mIsBound){
	        	mBoundService.executeSync();
	        }
    	}else {
    		Log.i(TAG, "No network connection");
    		
    		Toast.makeText(LauncherActivity.this, "No network!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public boolean isOnline() {
	   	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	   	NetworkInfo netInfo = cm.getActiveNetworkInfo();
	
	   	 if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;

   	}

    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalBinder) service).getService();
            mIsBound = true;
            Log.i(TAG, "sync service connected");
            // Toast.makeText(LauncherActivity.this, "Sync service connected", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            mIsBound = false;
            Log.i(TAG, "sync service disconnected");
            // Toast.makeText(LauncherActivity.this, "Sync service disconnected", Toast.LENGTH_SHORT).show();
        }
    };
    
    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();

			switch (msg.arg1) {
			case Activity.RESULT_OK:
				Toast
				.makeText(LauncherActivity.this, "Sync complete!",
						Toast.LENGTH_LONG)
						.show();
				
				break;

			case Activity.RESULT_CANCELED:
				if (msg.arg2 == Constants.MSG_AUTH_ERROR) {
					Toast
					.makeText(LauncherActivity.this, "Sync error! Authentication error",
							Toast.LENGTH_LONG)
							.show();
					logout();
				} else {
					Toast
					.makeText(LauncherActivity.this, "Unknown sync error! ",
							Toast.LENGTH_LONG)
							.show();
				}
				break;
			default:
				break;
			}
		}
	};

    
    /* Class that prevents opening the Browser */
    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //if URL comes from our server load in same web view otherwise open browser
        	if(url.toLowerCase().contains("gentileschi"))
            {
	        	view.loadUrl(url);
	            return true;
            } 
//        	else{
//            	Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
//                view.getContext().startActivity(intent);
//                return true;
//            }
        	return false;
        	
        }
    }
    


}