package de.dbis.microlearn.client.sync;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.dbis.microlearn.client.Constants;
import de.dbis.microlearn.client.NetworkTools;
import de.dbis.microlearn.client.model.Note;
import de.dbis.microlearn.client.model.DBAdapter;
import de.dbis.microlearn.client.model.Tagmap;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class SimpleSyncService extends Service implements Runnable {

	private static final String TAG = "SimpleSyncService";
	public static final String EXTRA_MESSENGER = "de.dbis.microlearn.client.EXTRA_MESSENGER";
	private ConnectivityManager cnnxManager;
	private NotificationManager mNM;

	// This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
	private Intent intent = null;
	
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
         public SimpleSyncService getService() {
            return SimpleSyncService.this;
        }
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		this.intent  = intent;

		return mBinder;
	}
	
	@Override
	  public void onCreate() {
	    super.onCreate();
	    Log.i(TAG, "Service creating");
	 
	    
	  }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");
		
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //this method is not called with service binding 
		Log.i(TAG, "Received start id " + startId + ": " + intent);
        this.intent  = intent;
        executeSync();
        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }


	public void executeSync() {
		 Log.i(TAG, "Sync service execute.");

		 Thread thread = new Thread(this);
		 thread.start();
		
		 
		 
		//get data from server
		//use NetworkTools
//		Thread t = NetworkTools.performSync();
		
	}

	public static void wait(int n){
        long t0,t1;
        t0=System.currentTimeMillis();
        do{
            t1=System.currentTimeMillis();
        }
        while (t1-t0<5000);
}

	@Override
	public void run() {
		Log.i(TAG, "Thread id: " + Thread.currentThread().getId());
//		wait(35);
		try {
			ArrayList<Note> clipArray = new ArrayList<Note>();
			String DBAsJSON = NetworkTools.fetchFullDB(1);
			
			JSONObject jObject = new JSONObject(DBAsJSON);
			JSONArray clips = jObject.getJSONArray("clips");
			for(int i=0; i<clips.length(); i++){
				Note c = new Note(
					clips.getJSONObject(i).getInt("clip_id"),
					clips.getJSONObject(i).getString("clip_title"),
					new String[] {clips.getJSONObject(i).getString("html_content")}, 
						new String[] {}
				);
				clipArray.add(c);
			}
			
			//Tagmap
			ArrayList<Tagmap> tmArray = new ArrayList<Tagmap>();
			JSONArray tagmap = jObject.getJSONArray("tagmap");
			for(int i=0; i<tagmap.length(); i++){
				Tagmap c = new Tagmap(
					tagmap.getJSONObject(i).getInt("clip_id"),
					tagmap.getJSONObject(i).getString("tag_name")
				);
				tmArray.add(c);
			}
			
			//save records to databse
			DBAdapter dba = new DBAdapter(this);
	        dba.open();
			dba.populateDB(clipArray, tmArray);
			dba.close();

			Log.i(TAG, "Downloaded!");
			handler.sendEmptyMessage(Constants.MSG_DOWNLOADED);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			// authentication failed, send message back to the caller activity and display the login activity
			Log.e(TAG, "Authentication exception!");
			handler.sendEmptyMessage(Constants.MSG_AUTH_ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Handler handler = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	            case Constants.MSG_DOWNLOADED:
	            	if(intent != null){
	       			 Bundle extras=intent.getExtras();
	       	
		       			if (extras!=null) {
		       				Messenger messenger=(Messenger)extras.get(EXTRA_MESSENGER);
		       				Message replyMsg=Message.obtain();
		       				
		       				int result = Activity.RESULT_OK;
		       				replyMsg.arg1=result;
		       	
		       				try {
		       					Log.i(TAG, "reply to sender. Result OK");
		       					messenger.send(replyMsg);
		       				}
		       				catch (android.os.RemoteException e1) {
		       					Log.w(getClass().getName(), "Exception sending message", e1);
			       			}
		       			}
		       		 }
	            	
	            	// What to do when ready, example:
	                break;
	            case Constants.MSG_AUTH_ERROR:
	            	if(intent != null){
		       			 Bundle extras=intent.getExtras();
		       	
			       			if (extras!=null) {
			       				Messenger messenger=(Messenger)extras.get(EXTRA_MESSENGER);
			       				Message replyMsg=Message.obtain();
			       				
			       				int result = Activity.RESULT_CANCELED;
			       				replyMsg.arg1=result;
			       				replyMsg.arg2=Constants.MSG_AUTH_ERROR;
			       	
			       				try {
			       					Log.i(TAG, "reply to sender. Auth Error");
			       					messenger.send(replyMsg);
			       				}
			       				catch (android.os.RemoteException e1) {
			       					Log.w(getClass().getName(), "Exception sending message", e1);
				       			}
			       			}
			       		 }
	            	break;
	            default:
	            	if(intent != null){
		       			 Bundle extras=intent.getExtras();
		       	
			       			if (extras!=null) {
			       				Messenger messenger=(Messenger)extras.get(EXTRA_MESSENGER);
			       				Message replyMsg=Message.obtain();
			       				
			       				int result = Activity.RESULT_CANCELED;
			       				replyMsg.arg1=result;
			       	
			       				try {
			       					messenger.send(replyMsg);
			       				}
			       				catch (android.os.RemoteException e1) {
			       					Log.w(getClass().getName(), "Exception sending message", e1);
				       			}
			       			}
			       		 }
	            	break;
	            }
	        }
	    };

	
}
