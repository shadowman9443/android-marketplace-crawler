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
package com.marketplace;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.marketplace.exceptions.ConnectivityException;
import com.marketplace.io.Sender;

/**
 * <code>Utils</code> is a helper class which creates dummy market number. It
 * also converts a list permissions to their respective value in the database to
 * prevent redundancy.
 * 
 * @author raunak
 * @version 1.0
 */
public class Utils {

	/**
	 * Instance of the <code>Gson</code> object
	 */
	public static final Gson gson = new Gson();

	/**
	 * Instance of the <code>Random</code> object
	 */
	public static final Random random = new Random();

	/**
	 * Instance of the <code>Log</code> object
	 */
	private static Log log = LogFactory.getLog(Utils.class);

	/**
	 * The name of file containing the most up to date list of permissions.
	 */
	private static String fileName = "permission";

	/**
	 * A list of permission
	 */
	private static HashMap<String, Integer> pMap = new Permission().getPMap();

	/**
	 * Converts a list of Android permissions to a corresponding int value in
	 * database. If a new permission is found, then that permission gets added
	 * to an external file 'permission' found in the directory config.
	 * 
	 * @param permissions
	 *            a list of permissions that needs to be converted.
	 * 
	 * @return a list of corresponding int value.
	 */
	synchronized public static List<Integer> permissionToInt(List<String> permissions) {
		List<Integer> pListArr = new ArrayList<Integer>(permissions.size());

		for (String permission : permissions) {
			if (pMap.containsKey(permission)) {
				pListArr.add(pMap.get(permission));
			} else {
				try {
					log.info("New permission found. Adding to the database");

					PrintStream printStream = new PrintStream(new FileOutputStream(Utils.fileName, true), true);
					printStream.print(permission + "\t" + (pMap.size() + 1) + "\n");
					printStream.close();

					pMap.put(permission, pMap.size() + 1);
					new Sender().doBasicHttpPost("{\"permission\":\"" + permission + "\"}", Constants.newPermissionUrlJson);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ConnectivityException e) {
					log.info("There was a problem connecting to the database");
				}
			}
		}

		return pListArr;
	}

	/**
	 * Generates a random long which is used as a dummy market id
	 * 
	 * @return a random long
	 * @referenced 
	 *             http://stackoverflow.com/questions/2546078/java-random-long-number-in-0-x-n-range/2546186#2546186
	 */
	public static long nextLong() {
		long bits, val;
		do {
			bits = (random.nextLong() << 1) >>> 1;
			val = bits % 10000000000000L;
		} while (bits - val + (10000000000000L - 1) < 0L);
		return val + 30000000000000L;
	}

	/**
	 * <code>Permission</code> contains all the Permissions that exist and can
	 * be used by an Android app. The permissions are loaded from an external
	 * file which is kept up to date by the crawler.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	private static class Permission {

		/**
		 * Contains all the Android App permission.
		 */
		private HashMap<String, Integer> pMap = new HashMap<String, Integer>(150);

		/**
		 * Instance of the <code>Log</code> object
		 */
		private Log log = LogFactory.getLog(Utils.class);

		/**
		 * Constructs a <code>Permission</code> object; Adds all the permissions
		 * to a hashmap.
		 */
		public Permission() {
			try {
				log.info("Loading permissions");
				BufferedReader bufferedReader = new BufferedReader(new FileReader(Utils.fileName));

				String permission = null;
				while ((permission = bufferedReader.readLine()) != null) {
					String[] token = permission.split("\t");
					try {
						pMap.put(token[0], Integer.parseInt(token[1]));
					} catch (NumberFormatException nfe) {
						log.info("Failed to parse permission " + token[0]);
					}
				}

				bufferedReader.close();

			} catch (IOException ioe) {
				log.info("File 'permission' was not found. Attempting to create file 'permission");
			}
		}

		/**
		 * Gets the HashMap of permissions
		 * 
		 * @return permission hashmap
		 */
		public HashMap<String, Integer> getPMap() {
			return pMap;
		}
	}

}
