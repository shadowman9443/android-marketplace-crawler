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
package com.marketplace.util;

/**
 * <code>User</code> is a representation of a real user who wishes to access the
 * Google Android Marketplace using their Google username
 * 
 * @author raunak
 * @version 1.0
 */
public class User {

	/**
	 * The username that the user wishes to use to access the Marketplace
	 */
	private String username;

	/**
	 * The password respective to the username in use
	 */
	private String password;

	/**
	 * A boolean flag which indicates if the username is currently being used
	 */
	private boolean status;

	/**
	 * Constructs a <code>User</code> object using the passed parameters.
	 * 
	 * @param username
	 *            The username that the user wishes to use to access the
	 *            Marketplace
	 * @param password
	 *            The password respective to the username in use
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.status = false;
	}

	/**
	 * Gets the username
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the status of the user in question. returns true, if the username is
	 * in use; false otherwise.
	 * 
	 * @return status
	 */
	public boolean inUse() {
		return status;
	}

	/**
	 * Set the status of username is question.
	 * 
	 * @param status
	 *            true if the username is in use; false otherwise
	 */
	public void inUse(boolean status) {
		this.status = status;
	}
}
