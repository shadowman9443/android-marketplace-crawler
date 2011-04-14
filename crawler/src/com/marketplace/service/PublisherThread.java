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
 * <code>PublisherThread</code> fetches app(s) from the Marketplace via
 * publisher name. A maximum of 800 apps per publisher can be fetched.
 * 
 * @author raunak
 * @version 1.0
 */
public class PublisherThread extends AppThread {

	/**
	 * <code>Log</code> object
	 */
	private Log log = LogFactory.getLog(PublisherThread.class);

	/**
	 * Publisher Name
	 */
	private String publisherName;

	/**
	 * Constructs a <code>PublisherThread</code> using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param publisherName
	 *            the name of publisher to fetch app(s) for
	 */
	public PublisherThread(Session session, String publisherName) {
		super(session);
		this.publisherName = publisherName;
	}

	/**
	 * Constructs a <code>PublisherThread</code> using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param publisherName
	 *            the name of publisher to fetch app(s) for
	 */
	public PublisherThread(Session session, Fetcher fetcher, String publisherName) {
		super(session, fetcher);
		this.publisherName = publisherName;
	}

	/**
	 * Constructs a <code>PublisherThread</code> using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param sender
	 *            the sender object to use for saving the app(s) to database
	 * @param publisherName
	 *            the name of publisher to fetch app(s) for
	 */
	public PublisherThread(Session session, Fetcher fetcher, Sender sender, String publisherName) {
		super(session, fetcher, sender);
		this.publisherName = publisherName;
	}

	@Override
	public void run() {
		log.info("Creating thread for pubisher " + publisherName);

		boolean hasNext = true;
		boolean setCount = false;
		int noOfPublishedApps = 800;
		AppsResponse appsResponse;

		while (hasNext) {
			try {
				appsResponse = fetcher.getAppByPublisher(session.getMarketSession(), this.publisherName, startIndex);

				if ((appsResponse != null) && (attempts < maxAttempts)) {
					if (!setCount) {
						noOfPublishedApps = appsResponse.getEntriesCount();
						setCount = true;
					}

					try {
						sender.addAppToCollection(appsResponse, session.getDevice().getDeviceVersion());
						startIndex += appsResponse.getAppCount();
						attempts = 0;
					} catch (ConnectivityException ce) {
						ce.printStackTrace();
					}
				} else if (attempts >= maxAttempts) {
					startIndex += 10;
				} else {
					attempts++;
				}

				// Sleep 1 ~ 3 minutes
				sleep(sleepTime + new Random().nextInt(120000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (startIndex >= noOfPublishedApps || noOfPublishedApps >= 800) {
				hasNext = false;
			}
		}

		sender.closeConnection();
		log.info("Thread execution completed : " + publisherName);
	}

}
