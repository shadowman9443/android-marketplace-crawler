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

import com.marketplace.Utils;

/**
 * <code>Device</code> holds information about a Virtual Android device.
 * 
 * @author raunak
 * @version 1.0
 * 
 */
public class Device {

	/**
	 * Identifier used by Google Android Marketplace to verify if a device is
	 * genuine
	 */
	private String marketId;

	/**
	 * The name of device
	 */
	private String deviceName;

	/**
	 * The version of Android the device is running.
	 */
	private int deviceVersion;

	/**
	 * Constructs a <code>Device</code> using the passed parameters.
	 * 
	 * @param deviceVersion
	 *            The version of Android the device is running.
	 * @param deviceName
	 *            The device name.
	 */
	public Device(int deviceVersion, String deviceName) {
		this.deviceName = deviceName;
		this.deviceVersion = deviceVersion;
		this.marketId = String.valueOf(Utils.nextLong());
	}

	/**
	 * Constructs a <code>Device</code> using the passed parameters.
	 * 
	 * @param deviceVersion
	 *            The version of Android the device is running.
	 * @param deviceName
	 *            The device name.
	 * @param marketId
	 *            Identifier used by Google Android Marketplace to verify if a
	 *            device is genuine
	 */
	public Device(int deviceVersion, String deviceName, String marketId) {
		this.deviceName = deviceName;
		this.deviceVersion = deviceVersion;
		this.marketId = marketId;
	}

	/**
	 * Gets the name of Android device.
	 * 
	 * @return deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Gets the version of android running on device.
	 * 
	 * @return deviceVersion
	 */
	public int getDeviceVersion() {
		return deviceVersion;
	}

	/**
	 * Gets the market id for device.
	 * 
	 * @return marketId
	 */
	public String getMarketId() {
		return marketId;
	}

	/**
	 * Set the market id for device
	 * 
	 * @param marketId
	 *            market id
	 */
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	@Override
	public String toString() {
		return deviceName + ":" + deviceVersion;
	}
}
