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

import com.gc.android.market.api.model.Market.CommentsResponse;
import com.marketplace.exceptions.ConnectivityException;
import com.marketplace.io.Fetcher;
import com.marketplace.io.Sender;
import com.marketplace.io.Session;

/**
 * <code>CommentThread</code> fetches comment(s) from the Marketplace for an
 * app.
 * 
 * @author raunak
 * @version 1.0
 */
public class CommentThread extends AppThread {

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
	 * Constructs a <code>CommentThread</code> using the passed parameters.
	 * 
	 * @param session
	 *            the session object to use for fetching comment(s)
	 * @param databaseId
	 *            id of app in database
	 * @param appId
	 *            id that Google gave to the app
	 */
	public CommentThread(Session session, int databaseId, String appId) {
		super(session);
		init(databaseId, appId);
	}

	/**
	 * Constructs a <code>CommentThread</code> using the passed parameters.
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
	public CommentThread(Session session, Fetcher fetcher, int databaseId, String appId) {
		super(session, fetcher);
		init(databaseId, appId);
	}

	/**
	 * Constructs a <code>CommentThread</code> using the passed parameters.
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
	public CommentThread(Session session, Fetcher fetcher, Sender sender, int databaseId, String appId) {
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

		boolean hasNext = true;
		boolean setCount = false;
		int noOfComments = 100;
		CommentsResponse commentsResponse;

		while (hasNext) {
			try {
				commentsResponse = fetcher.getAppComments(session.getMarketSession(), startIndex, appId);

				if (startIndex > noOfComments) {
					hasNext = false;
				}

				if ((commentsResponse != null) && (attempts < maxAttempts)) {

					// Only fetch the last 100 comments
					if (!setCount) {
						if (commentsResponse.getEntriesCount() < 100) {
							noOfComments = commentsResponse.getEntriesCount();
						}
						setCount = true;
					}

					try {
						sender.addCommentToCollection(commentsResponse, databaseId);
						startIndex += commentsResponse.getCommentsCount();
						attempts = 0;
					} catch (ConnectivityException ce) {
						ce.printStackTrace();
					}

				} else if (attempts >= maxAttempts) {
					startIndex += 10;
				} else {
					attempts++;
				}

				// Sleep 1 ~ 3 Minutes
				sleep(sleepTime + new Random().nextInt(120000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		log.info("Thread execution completed : " + appId);
	}
}
