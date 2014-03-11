package de.dbis.microlearn.client.auth;

import org.apache.http.HttpStatus;

import de.dbis.microlearn.client.Constants;
import de.dbis.microlearn.client.LauncherActivity;
import de.dbis.microlearn.client.NetworkTools;
import de.dbis.microlearn.client.R;
import de.dbis.microlearn.client.User;
import de.dbis.microlearn.client.authenticator.AuthenticatorActivity;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

public class AuthActivity extends Activity {

	private static final String TAG = "AuthActivity";
	
    /** for posting authentication attempts back to UI thread */
    private final Handler mHandler = new Handler();
    private TextView mMessage;
    private String mPassword;
    private EditText mPasswordEdit;

    private Thread mAuthThread;
    private String mAuthtoken;
    private String mAuthtokenType;
    
    private String mUsername;
    private EditText mUsernameEdit;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate(" + savedInstanceState + ")");
        
        setContentView(R.layout.login_activity);
        mMessage = (TextView) findViewById(R.id.message);
        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);

        
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.ui_activity_authenticating));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "dialog cancel has been invoked");
                if (mAuthThread != null) {
                    mAuthThread.interrupt();
                    finish();
                }
            }
        });
        return dialog;
    }
    
    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication.
     * 
     * @param view The Submit button for which this method is invoked
     */
    public void handleLogin(View view) {
    	Log.i(TAG, "Handle Login called");
        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
            showProgress();
            // Start authenticating...
            mAuthThread =
                NetworkTools.attemptAuth(mUsername, mPassword, mHandler, AuthActivity.this);
        }
    }
	
	public void onAuthenticationResult(Boolean result, int reason) {
        Log.i(TAG, "onAuthenticationResult(" + result + ")");
        // Hide the progress dialog
        hideProgress();
        if (result) {
                finishLogin();
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
                // "Please enter a valid username/password.
            switch (reason) {
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				mMessage.setText(getText(R.string.login_activity_loginfail_text_network_error));
				break;
			case HttpStatus.SC_UNAUTHORIZED:
				mMessage.setText(getText(R.string.login_activity_loginfail_text_both));
				break;
			default:
				mMessage.setText(getText(R.string.login_activity_general_error) + " " + reason);
				break;
			}
        }		
	}
	
    /**
     * 
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     * 
     * @param the confirmCredentials result.
     */

    protected void finishLogin() {
        Log.i(TAG, "finishLogin()");

//        final Intent intent = new Intent();
//        mAuthtoken = mPassword;
//        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
//        if (mAuthtokenType != null
//            && mAuthtokenType.equals(Constants.AUTHTOKEN_TYPE)) {
//            intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
//        }
//        setResult(RESULT_OK, intent);
        
        //set settings that user is logged in
        
//        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("logged", true);
        editor.putString("username", mUsername);
        editor.putString("password", mPassword);

        // Commit the edits!
        editor.commit();
        
        User.getInstance().setUsername(mUsername);
        User.getInstance().setPassword(mPassword);

        
        Intent i = new Intent(this, LauncherActivity.class);
        startActivity(i);
        finish();
    }
	
    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessage() {
        getString(R.string.label);
        if (TextUtils.isEmpty(mUsername)) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            final CharSequence msg =
                getText(R.string.login_activity_newaccount_text);
            return msg;
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.login_activity_loginfail_text_pwmissing);
        }
        return null;
    }
	
	   /**
     * Shows the progress UI for a lengthy operation.
     */
    protected void showProgress() {
        showDialog(0);
    }
    
    /**
     * Hides the progress UI for a lengthy operation.
     */
    protected void hideProgress() {
        dismissDialog(0);
    }

}
