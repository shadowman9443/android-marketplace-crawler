/* 
 * Copyright (c) 2011 Raunak Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.marketplace.io;

import java.util.prefs.Preferences;

import com.marketplace.util.User;

/**
 * <code>Secure</code> holds a list of username(s) and the respective password.
 * Its best to have more than one username.
 * 
 * @author raunak
 * @version 1.0
 */
public class Secure {

	private Preferences preferences = Preferences.userNodeForPackage(Secure.class);

	public Secure() {
		preferences.put("username_key", "username@gmail.com");
		// preferences.put("more username", "username1@gmail.com");
		// preferences.put("more username 2", "username2@gmail.com");
		preferences.put("password_key", "password");
	}

	public String getUsername() {
		return preferences.get("username_key", null);
	}

	public String getPassword() {
		return preferences.get("password_key", null);
	}

	/**
	 * Get an array of usernames
	 * 
	 * @return A username array
	 */
	public User[] getUsers() {
		/*
		 * Ideally, one would want 11+ username. 1 username per Android version,
		 * and the other username for crawling images and comments. For Testing
		 * purposes, one can add the same username to different index array.
		 */
		User[] users = new User[17];
		users[0] = new User(getUsername(), getPassword());
		users[1] = new User(getUsername(), getPassword());
		users[2] = new User(getUsername(), getPassword());
		users[3] = new User(getUsername(), getPassword());
		users[4] = new User(getUsername(), getPassword());
		users[5] = new User(getUsername(), getPassword());
		users[6] = new User(getUsername(), getPassword());
		users[7] = new User(getUsername(), getPassword());
		users[8] = new User(getUsername(), getPassword());
		users[9] = new User(getUsername(), getPassword());
		users[10] = new User(getUsername(), getPassword());
		users[11] = new User(getUsername(), getPassword());
		users[12] = new User(getUsername(), getPassword());
		users[13] = new User(getUsername(), getPassword());
		users[14] = new User(getUsername(), getPassword());
		users[15] = new User(getUsername(), getPassword());
		users[16] = new User(getUsername(), getPassword());

		return users;
	}

}

