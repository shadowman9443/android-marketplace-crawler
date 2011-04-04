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
 * <code>CategoryThread</code> fetches app(s) from the Marketplace via Category.
 * It can either fetch app(s) via popularity or by newest.
 * 
 * @author raunak
 * @version 1.0
 */
public class CategoryThread extends AppThread {

	/**
	 * <code>Log</code> object
	 */
	private Log log = LogFactory.getLog(CategoryThread.class);

	/**
	 * Name of category
	 */
	private String categoryName;

	/**
	 * Boolean flag; set to true if searching the market by newest apps, false
	 * otherwise
	 */
	private boolean newest;

	/**
	 * Constructs a <code>CategoryThread</code> using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param categoryName
	 *            the category to fetch app(s) for
	 * @param newest
	 *            a boolean flag. set to true if want to download newest app(s);
	 *            false otherwise.
	 */
	public CategoryThread(Session session, String categoryName, boolean newest) {
		super(session);
		init(newest, categoryName);
	}

	/**
	 * Constructs a <code>CategoryThread</code> using the passed parameters
	 * 
	 * @param session
	 *            the session to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param categoryName
	 *            the category to fetch app(s) for
	 * @param newest
	 *            a boolean flag. set to true if want to download newest app(s);
	 *            false otherwise.
	 */
	public CategoryThread(Session session, Fetcher fetcher, String categoryName, boolean newest) {
		super(session, fetcher);
		init(newest, categoryName);
	}

	/**
	 * Constructs a <code>CategoryThread</code> using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param sender
	 *            the sender object to use for saving the app(s) to database
	 * @param categoryName
	 *            the category to fetch app(s) for
	 * @param newest
	 *            a boolean flag. set to true if want to download newest app(s);
	 *            false otherwise.
	 */
	public CategoryThread(Session session, Fetcher fetcher, Sender sender, String categoryName, boolean newest) {
		super(session, fetcher, sender);
		init(newest, categoryName);

	}

	/**
	 * Helper Method; sets the category name, and a boolean flag 'newest'.
	 * 
	 * @param newest
	 *            a boolean flag. true if downloading newest app(s); false
	 *            otherwise.
	 * 
	 * @param categoryName
	 *            the category to fetch app(s) for
	 */
	private void init(boolean newest, String categoryName) {
		this.newest = newest;
		this.maxAppIndex = 800;
		this.categoryName = categoryName;
	}

	@Override
	public void run() {
		log.info("Creating thread for category " + this.categoryName);

		try {
			while (startIndex < maxAppIndex) {

				AppsResponse appsResponse = fetcher.getAppByCategory(startIndex, this.categoryName, this.newest, session.getMarketSession());

				if ((appsResponse != null) && (attempts < maxAttempts)) {
					try {
						sender.addAppToCollection(appsResponse, session.getDevice().getDeviceVersion());
						startIndex += appsResponse.getAppCount();
						attempts = 0;
					} catch (ConnectivityException ce) {
						log.info("An error occured while saving app response");
					}

				} else if (attempts >= maxAttempts) {
					startIndex += 10;
				} else {
					attempts++;
				}
				// Sleep for 1 ~ 3 Minutes
				sleep(sleepTime + new Random().nextInt(120000));
			}

			log.info("Thread execution completed : " + categoryName);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
