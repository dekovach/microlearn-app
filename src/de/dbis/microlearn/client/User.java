/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.dbis.microlearn.client;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Represents a sample SyncAdapter user
 */
public class User {

	public static final String USERNAME = "username"; 
	public static final String USER_PASSWORD = "password"; 
	public static final String AUTH_TOKEN = "authToken"; 
	
	private static User instance;
	private int mUserId;
    private String mUsername;
    private String mPassword;
    private String mAuthToken;
	
    private User(Integer userId, String username, String password, String authToken) {
    	mUserId = userId;
    	mUsername = username;
    	mPassword = password;
        mAuthToken = authToken;
    }
    
	public static synchronized User getInstance(){
		// Restore preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Microlearn.getAppContext());
		Integer userId = 1;
		String username = settings.getString(USERNAME, null); //"dejan";
		String password = settings.getString(USER_PASSWORD, null);
		String authToken = settings.getString(AUTH_TOKEN, null);

		if (User.instance == null) {
		    User.instance = new User(userId, username, password, authToken);
		} else {
			//refresh from options
//		    User.instance.setUsername(username);
//		    User.instance.setPassword(password);
//		    User.instance.setAuthToken(authToken);
		}
		return User.instance;
	}

    public int getUserId() {
        return mUserId;
    }
 
    public void setAuthToken(String authToken) {
		this.mAuthToken = authToken;
	}

	public String getAuthToken() {
		return mAuthToken;
	}

	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public String getPassword() {
		return mPassword;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String mUsername) {
		this.mUsername = mUsername;
	}

	//persist User
	public void persistUserCredentials() {
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Microlearn.getAppContext());
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(USERNAME, mUsername);
	    editor.putString(USER_PASSWORD, mPassword);
	    editor.putString(AUTH_TOKEN, mAuthToken);

	    // Commit the edits!
	    editor.commit();
	}
	
	public boolean isUserLogged() {
		if (!(this.mUsername == null || this.mUsername == "") 
				&& !(this.mAuthToken == null || this.mAuthToken == "")) 
			return true;
		
		return false;
	}
	
	public void logoutUser() {
//		mUsername = null;
		this.mPassword = null;
		this.mAuthToken = null;
		this.persistUserCredentials();
	}


}
