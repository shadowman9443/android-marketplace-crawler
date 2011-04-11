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

import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;
import com.marketplace.exceptions.ConnectivityException;
import com.marketplace.io.Fetcher;
import com.marketplace.io.Sender;
import com.marketplace.io.Session;

/**
 * <code>ImageThread</code> fetches icon from the Marketplace for an app. run()
 * can be extended to fetch screenshot(s).
 * 
 * @author raunak
 * @version 1.0
 */
public class ImageThread extends AppThread {

	/**
	 * <code>Log</code> object
	 */
	private Log log = LogFactory.getLog(CommentThread.class);

	/**
	 * The id of app in database. (needed by rails app)
	 */
	private int databaseId;

	/**
	 * app id (id that Google gave to the app)
	 */
	private String appId;

	/**
	 * Constructs a <code>ImageThread</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching comment(s)
	 * @param databaseId
	 *            id of app in database
	 * @param appId
	 *            id that Google gave to the app
	 */
	public ImageThread(Session session, int databaseId, String appId) {
		super(session);
		init(databaseId, appId);
	}

	/**
	 * Constructs a <code>ImageThread</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching comment(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the comment(s)
	 * @param databaseId
	 *            id of app in database
	 * @param appId
	 *            id that Google gave to the app
	 */
	public ImageThread(Session session, Fetcher fetcher, int databaseId, String appId) {
		super(session, fetcher);
		init(databaseId, appId);
	}

	/**
	 * Constructs a <code>ImageThread</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching comment(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the comment(s)
	 * @param sender
	 *            the sender object to use for saving the comment(s) to database
	 * @param databaseId
	 *            id of app in database
	 * @param appId
	 *            id that Google gave to the app
	 */
	public ImageThread(Session session, Fetcher fetcher, Sender sender, int databaseId, String appId) {
		super(session, fetcher, sender);
		init(databaseId, appId);
	}

	/**
	 * Helper method; Sets the database id and appId of an app.
	 * 
	 * @param databaseId
	 *            id of app in database
	 * @param appId
	 *            id that Google gave to the app
	 */
	private void init(int databaseId, String appId) {
		this.databaseId = databaseId;
		this.appId = appId;
	}

	@Override
	public void run() {
		log.info("Creating thread for appId: " + appId);

		byte[] imageResponse;
		boolean hasNext = true;

		while (hasNext) {
			try {
				/*
				 * To Get screenshot(s)
				 * fetcher.getImages(session.getMarketSession(), appId, AppImageUsage.SCREENSHOT, "0");
				 */

				imageResponse = fetcher.getAppVisuals(session.getMarketSession(), appId, AppImageUsage.ICON, "0");

				if ((imageResponse != null) && (attempts < maxAttempts)) {
					try {
						sender.addVisualToCollection(imageResponse, databaseId, "icon.png");
						hasNext = false;
					} catch (ConnectivityException e) {
						e.printStackTrace();
					}
				}

				if (attempts > maxAttempts) {
					hasNext = false;
				} else {
					attempts++;
				}

				sleep(sleepTime + new Random().nextInt(120000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		log.info("Thread execution completed : " + appId);
	}

}
