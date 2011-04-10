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
package com.marketplace.service;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gc.android.market.api.model.Market.AppsResponse;
import com.marketplace.exceptions.ConnectivityException;
import com.marketplace.io.Fetcher;
import com.marketplace.io.Sender;
import com.marketplace.io.Session;

/**
 * <code>PackageThread</code> fetches app(s) from the Marketplace via package
 * name.
 * 
 * @author raunak
 * @version 1.0
 */
public class PackageThread extends AppThread {

	/**
	 * <code>Log</code> object
	 */
	private Log log = LogFactory.getLog(PackageThread.class);

	/**
	 * Name of package
	 */
	private String packageName;

	/**
	 * Constructs a <code>PackageThead</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param packageName
	 *            the name of package to fetch
	 */
	public PackageThread(Session session, String packageName) {
		super(session);
		this.packageName = packageName;
	}

	/**
	 * Constructs a <code>PackageThead</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param packageName
	 *            the name of package to fetch
	 */
	public PackageThread(Session session, Fetcher fetcher, String packageName) {
		super(session, fetcher);
		this.packageName = packageName;
	}

	/**
	 * Constructs a <code>PackageThead</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param sender
	 *            the sender object to use for saving the app(s) to database
	 * @param packageName
	 *            the name of package to fetch
	 */
	public PackageThread(Session session, Fetcher fetcher, Sender sender, String packageName) {
		super(session, fetcher, sender);
		this.packageName = packageName;
	}

	@Override
	public void run() {
		log.info("Creating thread for package: " + packageName + "\t API Level: " + this.session.getDevice().getDeviceVersion());

		try {
			AppsResponse appsResponse = fetcher.getAppsByPackageName(session.getMarketSession(), packageName);
			if (appsResponse != null) {
				try {
					sender.addAppToCollection(appsResponse, session.getDevice().getDeviceVersion());
				} catch (ConnectivityException ce) {
					ce.printStackTrace();
				}
			}
			
			sleep(sleepTime + new Random().nextInt(120000));
			log.info("Thread execution completed : " + packageName + "\t API Level: " + this.session.getDevice().getDeviceVersion());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
