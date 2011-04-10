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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.marketplace.exceptions.SessionUnavailableException;
import com.marketplace.exceptions.UserUnavailableException;
import com.marketplace.util.DeviceInventory;
import com.marketplace.util.User;

/**
 * <code>SessionManager</code> manages <code>Session</code> objects which are
 * used to access the Google Android Marketplace. The
 * <code>SessionManager</code> is capable of generating new <code>Session</code>
 * should one corrupts as well as provide basic methods such as
 * <code>getSession</code> which returns a Session object.
 * 
 * @author raunak
 * @version 1.0
 */
public class SessionManager {

	/**
	 * <code>Log</code> object
	 */
	private Log log = LogFactory.getLog(SessionManager.class);

	/**
	 * A list of user(s) that can be used by <code>Session</code>s
	 */
	private User[] users;

	/**
	 * A list of <code>Session</code> that can be used to access the Marketplace
	 */
	private Session[] sessions;

	/**
	 * Constructs a <code>SessionManager</code> It initializes the users array,
	 * as well as create <code>Session</code>
	 */
	public SessionManager() {
		this.users = new Secure().getUsers();
		this.sessions = initSessions();
	}

	/**
	 * Initializes the <code>Session</code> array. (1 Session per available API
	 * available).
	 * 
	 * @return an array of <code>Session</code>
	 */
	public Session[] initSessions() {
		Session[] sessions = new Session[DeviceInventory.getAllDevices().size()];
		for (int i = 0; i < sessions.length; i++) {
			try {
				sessions[i] = createNewSession(i + 1);
			} catch (UserUnavailableException e) {
				log.info("UserUnavailable exception was thrown during init process");
				e.printStackTrace();
			}
		}
		return sessions;
	}
	
	/**
	 * Initializes the <code>Session</code> array using the passed parameter.
	 * All the Sessions in the session array will be for this API Level.
	 * 
	 * @param apiLevel
	 * 
	 * @return an array of <code>Session</code>
	 */
	public Session[] initSessions(int apiLevel) {
		Session[] sessions = new Session[DeviceInventory.getAllDevices().size()];
		
		for (int i = 0; i < sessions.length; i++) {
			try {
				sessions[i] = createNewSession(apiLevel);
			} catch (UserUnavailableException e) {
				log.info("UserUnavailable exception was thrown during init process");
				e.printStackTrace();
			}
		}
		return sessions;
	}

	/**
	 * 
	 * @param apiLevel
	 * @return
	 * @throws UserUnavailableException
	 */
	public Session createNewSession(int apiLevel) throws UserUnavailableException {
		return new Session(DeviceInventory.getDevice(apiLevel), getNextUser());
	}

	/**
	 * 
	 * @return
	 * @throws UserUnavailableException
	 */
	private User getNextUser() throws UserUnavailableException {
		for (User user : users) {
			if (!user.inUse()) {
				user.inUse(true);
				return user;
			}
		}
		throw new UserUnavailableException();
	}

	/**
	 * Gets the <code>Session</code> object corresponding to the passed
	 * parameter.
	 * 
	 * @param apiLevel
	 * @return
	 * @throws SessionUnavailableException
	 */
	public Session getSession(int apiLevel) throws SessionUnavailableException {

		if ((apiLevel < 1) || (apiLevel > (sessions.length + 1))) {
			throw new IllegalAccessError("Android API Level " + apiLevel + " is not supported by this library.");
		}

		refreshSessions();

		for (Session session : sessions) {
			if (session.getDevice().getDeviceVersion() >= apiLevel) {
				return session;
			}
		}

		throw new SessionUnavailableException("Session for api level " + apiLevel + " is not available");
	}

	/**
	 * Gets all <code>Session</code>
	 * 
	 * @return all sessions
	 */
	public Session[] getSessions() {
		return sessions;
	}

	/**
	 * Insures that all <code>Session<code> objects are valid at all times.
	 * Should one or more becomes corrupted, replace it by a new one.
	 */
	private void refreshSessions() {
		for (int i = 0; i < sessions.length; i++) {
			if (sessions[i].isStale()) {
				try {
					log.info("Replacing a corrupted Session at index: " + i);

					sessions[i].getUser().inUse(false);
					sessions[i] = createNewSession(sessions[i].getDevice().getDeviceVersion());
				} catch (UserUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
