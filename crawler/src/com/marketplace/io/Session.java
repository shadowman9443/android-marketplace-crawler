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

import com.gc.android.market.api.MarketSession;
import com.marketplace.util.Device;
import com.marketplace.util.User;

/**
 * A <code>Session</code> object can be seen as a virtual Android Device. It
 * stores information on the user, the device they using (device name + android
 * version), and the current state of session - whether its being used or not.
 * 
 * When a <code>MarketSession</code> (a session which allows data to be
 * transferred from the marketplace) is expired, the <code>Session</code> class
 * is capable creating a new one.
 * 
 * @author raunak
 * @version 1.0
 */
public class Session {

	/**
	 * A <code>User</code> object, holding information on a real user who wishes
	 * to access the Google Android Marketplace using their Google username
	 */
	private User user;

	/**
	 * A <code>Device</code> object holding information on a virtual Android
	 * device.
	 */
	private Device device;

	/**
	 * a boolean flag. set to true if the session is in use; false otherwise
	 */
	private boolean status;

	/**
	 * a boolean flag. set to true if the session is corrupted; false otherwise
	 */
	private boolean stale;

	/**
	 * <code>MarketSession</code> is a gateway between the crawler and the
	 * Android Marketplace. A valid marketSession object allows one to pull
	 * information from the Android Marketplace.
	 */
	private MarketSession marketSession;

	/**
	 * Constructs a <code>Session</code> using the passed parameters
	 * 
	 * @param device
	 *            the device associated with the <code>Session</code>. See
	 *            <code>DeviceInventory.java</code>
	 * @param user
	 *            the user associated with the <code>Session</code>.
	 */
	public Session(Device device, User user) {
		this.stale = false;
		this.status = false; // Makes this session object available for use.

		this.user = user;
		this.device = device;

		this.marketSession = new MarketSession();
		this.marketSession.login(user.getUsername(), user.getPassword());
		this.marketSession.getContext().setAndroidId(device.getMarketId());
		this.marketSession.getContext().setDeviceAndSdkVersion(device.toString());
	}

	/**
	 * Gets the validity of <code>Session</code> object
	 * 
	 * @return stale; a boolean
	 */
	public boolean isStale() {
		return stale;
	}

	/**
	 * Set the validity of <code>Session</code> object
	 * 
	 * @param stale
	 *            current state of <code>Sesssion</code>
	 */
	public void isStale(boolean stale) {
		this.stale = stale;
	}

	/**
	 * Gets the availability of <code>Session</code> object
	 * 
	 * @return status; a boolean
	 */
	public boolean inUse() {
		return status;
	}

	/**
	 * Sets the availability of <code>Session</code> object
	 * 
	 * @param status
	 *            current state of <code>Session</code> object
	 */
	public void inUse(boolean status) {
		this.status = status;
	}

	/**
	 * Gets the <code>User</code> object associated with this
	 * <code>Session</code
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Gets the <code>Device</code> object associated with this
	 * <code>Session</code
	 * 
	 * @return device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * Gets the <code>MarketSession</code> object associated with this
	 * <code>Session</code
	 * 
	 * @return marketSession
	 */
	public MarketSession getMarketSession() {
		return marketSession;
	}

	/**
	 * Sets the <code>MarketSession</code> object associated with this
	 * <code>Session</code>
	 * 
	 * @param marketSession
	 *            a valid <code>MarketSession</code> object which can be used to
	 *            access Google Android Marketplace
	 */
	public void setMarketSession(MarketSession marketSession) {
		this.stale = false;
		this.status = true;
		this.marketSession = marketSession;
	}

	/**
	 * Refreshes the <code>MarketSession</code> object
	 */
	public void refreshMarketSession() {
		this.stale = false;
		this.status = true;

		this.marketSession = new MarketSession();
		this.marketSession.login(user.getUsername(), user.getPassword());
		this.marketSession.getContext().setAndroidId(device.getMarketId());
		this.marketSession.getContext().setDeviceAndSdkVersion(device.toString());
	}
}
