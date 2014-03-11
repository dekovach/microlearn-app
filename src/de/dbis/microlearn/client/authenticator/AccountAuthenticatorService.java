/*******************************************************************************
 * Copyright 2010 Sam Steele 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.dbis.microlearn.client.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AccountAuthenticatorService extends Service {
	private static final String TAG = "AccountAuthenticatorService";
	private static AccountAuthenticatorImpl sAccountAuthenticator = null;

	public AccountAuthenticatorService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
			ret = getAuthenticator().getIBinder();
		return ret;
	}

	private AccountAuthenticatorImpl getAuthenticator() {
		if (sAccountAuthenticator == null)
			sAccountAuthenticator = new AccountAuthenticatorImpl(this);
		return sAccountAuthenticator;
	}

	private static class AccountAuthenticatorImpl extends AbstractAccountAuthenticator {
		private Context mContext;

		public AccountAuthenticatorImpl(Context context) {
			super(context);
			mContext = context;
		}

		/*
         *  The user has requested to add a new account to the system.  We return an intent that will launch our login screen if the user has not logged in yet,
         *  otherwise our activity will just pass the user's credentials on to the account manager.
         */
		@Override
		public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options)
				throws NetworkErrorException {
			Bundle result = new Bundle();
			
			Intent i = new Intent(mContext, AuthenticatorActivity.class);
			
			i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
			result.putParcelable(AccountManager.KEY_INTENT, i);

			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.accounts.AbstractAccountAuthenticator#confirmCredentials(
		 * android.accounts.AccountAuthenticatorResponse,
		 * android.accounts.Account, android.os.Bundle)
		 */
		@Override
		public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
			Log.i(TAG, "confirmCredentials");
			
			Bundle reply = new Bundle();

            Intent i = new Intent(mContext, AuthenticatorActivity.class);
            i.setAction("com.martineve.mendroid.CreateMendeleyAccount");
            
            i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            reply.putParcelable(AccountManager.KEY_INTENT, i);
            
            return reply;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.accounts.AbstractAccountAuthenticator#editProperties(android
		 * .accounts.AccountAuthenticatorResponse, java.lang.String)
		 */
		@Override
		public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
			// TODO Auto-generated method stub
			Log.i(TAG, "editProperties");
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.accounts.AbstractAccountAuthenticator#getAuthToken(android
		 * .accounts.AccountAuthenticatorResponse, android.accounts.Account,
		 * java.lang.String, android.os.Bundle)
		 */
		@Override
		public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
			// TODO Auto-generated method stub
			Log.i(TAG, "getAuthToken");
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.accounts.AbstractAccountAuthenticator#getAuthTokenLabel(java
		 * .lang.String)
		 */
		@Override
		public String getAuthTokenLabel(String authTokenType) {
			// TODO Auto-generated method stub
			Log.i(TAG, "getAuthTokenLabel");
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.accounts.AbstractAccountAuthenticator#hasFeatures(android
		 * .accounts.AccountAuthenticatorResponse, android.accounts.Account,
		 * java.lang.String[])
		 */
		@Override
		public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
			// TODO Auto-generated method stub
			Log.i(TAG, "hasFeatures: " + features);
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.accounts.AbstractAccountAuthenticator#updateCredentials(android
		 * .accounts.AccountAuthenticatorResponse, android.accounts.Account,
		 * java.lang.String, android.os.Bundle)
		 */
		@Override
		public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) {
			// TODO Auto-generated method stub
			Log.i(TAG, "updateCredentials");
			return null;
		}
	}
}
