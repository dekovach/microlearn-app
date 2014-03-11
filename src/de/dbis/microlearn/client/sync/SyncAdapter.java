package de.dbis.microlearn.client.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import de.dbis.microlearn.client.Constants;
import de.dbis.microlearn.client.User;
//import com.example.android.samplesync.platform.ContactManager;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * SyncAdapter implementation for syncing sample SyncAdapter clips to the
 * application Clips provider.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";

    private final AccountManager mAccountManager;
    private final Context mContext;

    private Date mLastUpdated;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "performSync invoked");
    	List<User> users;
        String authtoken = null;
//         try {
             // use the account manager to request the credentials
//             authtoken = mAccountManager.blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE, true /* notifyAuthFailure */);
            
             // fetch updates from the sample service over the cloud
            // users = NetworkUtilities.fetchFriendUpdates(account, authtoken, mLastUpdated);
            // update the last synced date.
//            mLastUpdated = new Date();
            // update platform contacts.
//            Log.d(TAG, "Calling DataSyncManager to sync clips");
//            ContactManager.syncContacts(mContext, account.name, users);
            // fetch and update status messages for all the synced users.
//            statuses = NetworkUtilities.fetchFriendStatuses(account, authtoken);
//            ContactManager.insertStatuses(mContext, account.name, statuses);
//        } catch (final AuthenticatorException e) {
//            syncResult.stats.numParseExceptions++;
//            Log.e(TAG, "AuthenticatorException", e);
//        } catch (final OperationCanceledException e) {
//            Log.e(TAG, "OperationCanceledExcetpion", e);
//        } catch (final IOException e) {
//            Log.e(TAG, "IOException", e);
//            syncResult.stats.numIoExceptions++;
//        } catch (final AuthenticationException e) {
//            mAccountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE,
//                authtoken);
//            syncResult.stats.numAuthExceptions++;
//            Log.e(TAG, "AuthenticationException", e);
//        } catch (final ParseException e) {
//            syncResult.stats.numParseExceptions++;
//            Log.e(TAG, "ParseException", e);
//        } catch (final JSONException e) {
//            syncResult.stats.numParseExceptions++;
//            Log.e(TAG, "JSONException", e);
//        }
    }
}
