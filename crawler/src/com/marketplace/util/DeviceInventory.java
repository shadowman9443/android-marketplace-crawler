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

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of <code>Device</code> objects which can be accessed by the name
 * of Android version.
 * 
 * @author raunak
 * @version 1.0
 */
public class DeviceInventory {

	/**
	 * Device name: HTC G1 </br> Android Version: 1.0
	 */
	public static final Device VERSION1_0 = new Device(AndroidVersion.VERSION1_0, "g1");

	/**
	 * Device name: HTC G1 </br> Android Version: 1.1.
	 */
	public static final Device VERSION1_1 = new Device(AndroidVersion.VERSION1_1, "g1");

	/**
	 * Device name: HTC G1 </br> Android Version: 1.5 - Cupcake.
	 */
	public static final Device CUPCAKE = new Device(AndroidVersion.CUPCAKE, "g1");

	/**
	 * Device name: HTC Tattoo </br> Android Version: 1.6 - Cupcake.
	 */
	public static final Device DONUT = new Device(AndroidVersion.DONUT, "tattoo");

	/**
	 * Device name: Motorola Milestone </br> Android Version: 2.0 - Eclair.
	 */
	public static final Device ECLAIR = new Device(AndroidVersion.ECLAIR, "milestone");

	/**
	 * Device name: Motorola Milestone </br> Android Version: 2.0.1 -
	 * Eclair-update1.
	 */
	public static final Device ECLAIR_UPDATE1 = new Device(AndroidVersion.ECLAIR_UPDATE1, "milestone");

	/**
	 * Device name: HTC Hero </br> Android Version: 2.1- Eclair-update2.
	 */
	public static final Device ECLAIR_UPDATE2 = new Device(AndroidVersion.ECLAIR_UPDATE2, "hero");

	/**
	 * Device name: HTC Passion </br> Android Version: 2.2 - Froyo.
	 */
	public static final Device FROYO = new Device(AndroidVersion.FROYO, "passion");

	/**
	 * Device name: HTC Nexus 1</br> Android Version: 2.3 - Gingerbread.
	 */
	public static final Device GINGERBREAD = new Device(AndroidVersion.GINGERBREAD,"nexus");

	/**
	 * Device name: HTC Nexus S</br> Android Version: 2.3.3 -
	 * Gingerbread-update1.
	 */
	public static final Device GINGERBREAD_UPDATE1 = new Device(AndroidVersion.GINGERBREAD_UPDATE1, "nexuss");

	/**
	 * Device name: Motorola XOOM</br> Android Version: 3.0 - Honeycomb.
	 */
	public static final Device HONEYCOMB = new Device(AndroidVersion.HONEYCOMB, "xoom");

	/**
	 * Gets the <code>Device</code> object for the respective api level.
	 * 
	 * @param api_level
	 *            The version of android.
	 * @return <code>Device</code> object.
	 */
	public static Device getDevice(int api_level) {
		switch (api_level) {
		case 1:
			return DeviceInventory.VERSION1_0;
		case 2:
			return DeviceInventory.VERSION1_1;
		case 3:
			return DeviceInventory.CUPCAKE;
		case 4:
			return DeviceInventory.DONUT;
		case 5:
			return DeviceInventory.ECLAIR;
		case 6:
			return DeviceInventory.ECLAIR_UPDATE1;
		case 7:
			return DeviceInventory.ECLAIR_UPDATE2;
		case 8:
			return DeviceInventory.FROYO;
		case 9:
			return DeviceInventory.GINGERBREAD;
		case 10:
			return DeviceInventory.GINGERBREAD_UPDATE1;
		case 11:
			return DeviceInventory.HONEYCOMB;
		default:
			return DeviceInventory.ECLAIR_UPDATE2;
		}
	}

	/**
	 * Get all Android devices
	 * 
	 * @return a list of devices
	 */
	public static List<Device> getAllDevices() {
		List<Device> device = new ArrayList<Device>(11);
		
		device.add(getDevice(1));
		device.add(getDevice(2));
		device.add(getDevice(3));
		device.add(getDevice(4));
		device.add(getDevice(5));
		device.add(getDevice(6));
		device.add(getDevice(7));
		device.add(getDevice(8));
		device.add(getDevice(9));
		device.add(getDevice(10));
		device.add(getDevice(11));

		return device;
	}
}