package de.dbis.microlearn.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import de.dbis.microlearn.client.auth.AuthActivity;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class NetworkTools {
    private static final String TAG = "NetworkTools";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_AUTHTOKEN = "authtoken";
    public static final String PARAM_UPDATED = "timestamp";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    public static final String BASE_URL = "http://gentileschi.informatik.rwth-aachen.de:8080/Microlearn-server/rest";
    public static final String AUTH_URI = BASE_URL + "/auth/login";
	public static final String FETCH_FULLDB_URI = BASE_URL + "/full-sync";
    private static HttpClient mHttpClient;

    /**
     * Configures the httpClient to connect to the URL provided.
     */
    public static void maybeCreateHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
            ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
//            mHttpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android");
        }
    }
    
    /**
     * Executes the network requests on a separate thread.
     * 
     * @param runnable The runnable instance containing network mOperations to
     *        be executed.
     */
    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }
    
    /**
     * Connects to the server, authenticates the provided username and
     * password.
     * 
     * @param username The user's username
     * @param password The user's password
     * @param handler The hander instance from the calling UI thread.
     * @param context The context of the calling Activity.
     * @return boolean The boolean result indicating whether the user was
     *         successfully authenticated.
     */
    public static boolean authenticate(String username, String password, Handler handler, final Context context) {
        final HttpResponse resp;

        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, username));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (final UnsupportedEncodingException e) {
            // this should never happen.
            throw new AssertionError(e);
        }
        final HttpPost post = new HttpPost(AUTH_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();

        Log.i(TAG, "Attempt to authenticate username: " + username);
        
        try {
            resp = mHttpClient.execute(post);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.i(TAG, "Successful authentication");
                }
                sendResult(true, handler, context, resp.getStatusLine().getStatusCode());
                return true;
            } else {
//                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.i(TAG, "Error authenticating" + resp.getStatusLine());
//                }
                sendResult(false, handler, context, resp.getStatusLine().getStatusCode());
                return false;
            }
        } catch (final IOException e) {
//            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "IOException when getting authtoken", e);
//            }
            sendResult(false, handler, context, HttpStatus.SC_SERVICE_UNAVAILABLE);
            return false;
        } finally {
//            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "getAuthtoken completing");
//            }
        }
    }
    
    /**
     * Sends the authentication response from server back to the caller main UI
     * thread through its handler.
     * 
     * @param result The boolean holding authentication result
     * @param handler The main UI thread's handler instance.
     * @param context The caller Activity's context.
     */
    private static void sendResult(final Boolean result, final Handler handler, final Context context, final int reason) {
        if (handler == null || context == null) {
            return;
        }
        handler.post(new Runnable() {
            public void run() {
                ((AuthActivity) context).onAuthenticationResult(result, reason);
            }
        });
    }
    
    /**
     * Attempts to authenticate the user credentials on the server.
     * 
     * @param username The user's username
     * @param password The user's password to be authenticated
     * @param handler The main UI thread's handler instance.
     * @param context The caller Activity's context
     * @return Thread The thread on which the network mOperations are executed.
     */
    public static Thread attemptAuth(final String username,
        final String password, final Handler handler, final Context context) {
        final Runnable runnable = new Runnable() {
            public void run() {
                authenticate(username, password, handler, context);
            }
        };
        // run on background thread.
        return NetworkTools.performOnBackgroundThread(runnable);
    }
    
    public static String fetchFullDB(long lastSync) throws IOException, AuthenticationException{
    	final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	final String authToken = User.getInstance().getAuthToken();
    	final String username = User.getInstance().getUsername();
    	final String password = User.getInstance().getPassword();

    	params.add(new BasicNameValuePair(PARAM_USERNAME, username));
    	params.add(new BasicNameValuePair(PARAM_AUTHTOKEN, authToken));
    	params.add(new BasicNameValuePair(PARAM_PASSWORD, password));

        HttpEntity entity = null;
        entity = new UrlEncodedFormEntity(params);
        final HttpPost post = new HttpPost(FETCH_FULLDB_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();

        final HttpResponse resp = mHttpClient.execute(post);
        final String response = EntityUtils.toString(resp.getEntity());

        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // Successfully connected to the server and authenticated.
            Log.i(TAG, response);
        	return response;
        } else {
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG,
                    "Authentication exception in fetching database!");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in fetching full database!");
                throw new IOException();
            }
        }
    }
}
