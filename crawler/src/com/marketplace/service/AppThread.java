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

import com.marketplace.io.Fetcher;
import com.marketplace.io.Sender;
import com.marketplace.io.Session;

/**
 * <code>AppThread</code> is a base class.
 * 
 * @author raunak
 * @version 1.0
 */
public abstract class AppThread extends Thread {

	/**
	 * Starting index for marketplace query.
	 */
	protected int startIndex;

	/**
	 * Maximum number the start index can reach
	 */
	protected int maxAppIndex;

	/**
	 * the fetcher object used to fetch app(s)
	 */
	protected Fetcher fetcher;

	/**
	 * the sender object used to save app(s) to database
	 */
	protected Sender sender;

	/**
	 * the session object used to fetch app(s)
	 */
	protected Session session;

	/**
	 * Number of attempts the current query has made so far
	 */
	protected int attempts;

	/**
	 * Maximum number of attempts a query can make
	 */
	protected final int maxAttempts = 5;

	/**
	 * Sleep time between each marketplace query.
	 */
	protected final int sleepTime = 60000;

	/**
	 * Constructs <code>AppThread</code>using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 */
	public AppThread(Session session) {
		this(session, new Fetcher(), new Sender());
	}

	/**
	 * Constructs <code>AppThread</code>using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 */
	public AppThread(Session session, Fetcher fetcher) {
		this(session, fetcher, new Sender());
	}

	/**
	 * Constructs <code>AppThread</code>using the passed parameters
	 * 
	 * @param session
	 *            the session object to use for fetching the app(s)
	 * @param fetcher
	 *            the fetcher object to use for fetching the app(s)
	 * @param sender
	 *            the sender object to use for saving the app(s) to database
	 */
	public AppThread(Session session, Fetcher fetcher, Sender sender) {
		this.session = session;
		this.fetcher = fetcher;
		this.sender = sender;
	}

	@Override
	public void run() {
	}
}
