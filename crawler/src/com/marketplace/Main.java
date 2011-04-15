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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.marketplace.exceptions.ConnectivityException;
import com.marketplace.exceptions.UserUnavailableException;
import com.marketplace.io.Fetcher;
import com.marketplace.io.Fetcher.NextAppResponse;
import com.marketplace.io.Session;
import com.marketplace.io.SessionManager;
import com.marketplace.service.CategoryThread;
import com.marketplace.service.CommentThread;
import com.marketplace.service.ImageThread;
import com.marketplace.service.PackageThread;
import com.marketplace.service.PublisherThread;
import com.marketplace.util.AndroidVersion;

/**
 * Entry point of Application
 * 
 * @author raunak
 * @version 1.0
 */
public class Main {

	/**
	 * Instance of <code>Log</code> class.
	 */
	private Log log = LogFactory.getLog(Main.class);

	private boolean category = false;
	private boolean comment = false;
	private boolean image = false;
	private boolean latest = false;
	private boolean packages = false;
	private String packageFile = null;
	private boolean publisher = false;
	private String publisherFile = null;

	private SessionManager sessionManager;

	private static int imageIndex = 0;
	private static int commentIndex = 0;

	public Main() {
		this.sessionManager = new SessionManager();
	}

	/**
	 * Creates <code>CategoryThread</code> for each Android Marketplace
	 * Category.
	 * 
	 * @return a set containing <code>CategoryThread</code>
	 */
	private Set<Future<?>> createCategoryThread() {
		log.info("Creating threads for fetching apps via category.");

		Fetcher fetcher = new Fetcher();
		ExecutorService executorService = Executors.newFixedThreadPool(22);

		Set<Future<?>> set = new HashSet<Future<?>>();
		Session[] sessions = this.sessionManager.getSessions();
		Iterator<String> categories = Category.getAllCategories();

		for (int i = sessions.length; i > 0; i--) {
			while (categories.hasNext()) {
				set.add(executorService.submit(new CategoryThread(sessions[i - 1], fetcher, categories.next(), this.latest)));
			}
			categories = Category.getAllCategories();
		}

		return set;
	}

	/**
	 * Creates <code>PackageThread</code> for each package name found in the
	 * file.
	 * 
	 * @return a set containing <code>PackageThread</code>
	 */
	private Set<Future<?>> createPackageThread() {
		log.info("Creating threads for fetching apps via packages.");

		if (this.packageFile == null) {
			System.err.println("Please pass the location of file");
			System.exit(1);
		}

		Fetcher fetcher = new Fetcher();
		ExecutorService executorService = Executors.newFixedThreadPool(22);

		Set<Future<?>> set = new HashSet<Future<?>>();
		Session[] sessions = this.sessionManager.getSessions();
		List<String> packageNames = fetcher.readFile(this.packageFile);
		Iterator<String> iterator = packageNames.iterator();

		for (int i = sessions.length; i > 0; i--) {
			while (iterator.hasNext()) {
				set.add(executorService.submit(new PackageThread(sessions[i - 1], fetcher, iterator.next())));
			}
			iterator = packageNames.iterator();
		}

		return set;
	}

	/**
	 * Creates <code>PublisherThread</code> for each publisher found in the
	 * file.
	 * 
	 * @return a set containing <code>PublisherThread</code>
	 */
	private Set<Future<?>> createPublisherThread() {
		log.info("Creating threads for fetching apps via publishers." );

		if (this.publisherFile == null) {
			System.err.println("Please pass the location of file");
			System.exit(1);
		}

		Fetcher fetcher = new Fetcher();
		ExecutorService executorService = Executors.newFixedThreadPool(22);

		Set<Future<?>> set = new HashSet<Future<?>>();
		Session[] sessions = this.sessionManager.getSessions();
		Iterator<String> pubNames = fetcher.readFile(this.publisherFile).iterator();

		for (int i = sessions.length; i > 0; i--) {
			while (pubNames.hasNext()) {
				set.add(executorService.submit(new PublisherThread(sessions[i - 1], fetcher, pubNames.next())));
			}
			pubNames = fetcher.readFile(this.publisherFile).iterator();
		}

		return set;
	}

	/**
	 * Creates <code>ImageThread</code> for each app stored in the database.
	 * 
	 * @return a set containing <code>ImageThread</code>
	 */
	private Set<Future<?>> createImageThread() {
		log.info("Creating threads for fetching images for app(s)");

		Fetcher fetcher = new Fetcher();
		ExecutorService executorService = Executors.newFixedThreadPool(15);

		Set<Future<?>> set = new HashSet<Future<?>>();
		List<Session> imageSessions = new LinkedList<Session>();

		try {
			// Create 3 new Gingerbread Sessions
			for (int i = 0; i < 3; i++) {
				imageSessions.add(this.sessionManager.createNewSession(AndroidVersion.GINGERBREAD));
			}

			CyclicIterator<Session> sessions = new CyclicIterator<Session>(imageSessions);

			NextAppResponse[] appsResponse = fetcher.getNextAppIds(Main.imageIndex);
			if (appsResponse.length == 0) {
				log.info("Reached the end of collection. Reseting the Start index to 0.");

				Main.imageIndex = 0;
				appsResponse = fetcher.getNextAppIds(Main.imageIndex);
			}

			for (NextAppResponse nextAppResponse : appsResponse) {
				set.add(executorService.submit(new ImageThread(sessions.next(), fetcher, nextAppResponse.app.id, nextAppResponse.app.appId)));
			}

		} catch (UserUnavailableException e) {
			e.printStackTrace();
		} catch (ConnectivityException e) {
			e.printStackTrace();
		}

		return set;
	}

	/**
	 * Creates <code>CommentThread</code> for each app stored in the database.
	 * 
	 * @return a set containing <code>CommentThread</code>
	 */
	private Set<Future<?>> createCommentThread() {
		log.info("Creating threads for fetching comment(s) for app(s)");

		Fetcher fetcher = new Fetcher();
		ExecutorService executorService = Executors.newFixedThreadPool(15);

		Set<Future<?>> set = new HashSet<Future<?>>();
		List<Session> imageSessions = new LinkedList<Session>();

		try {
			// Create 3 new Gingerbread Sessions
			for (int i = 0; i < 3; i++) {
				imageSessions.add(this.sessionManager.createNewSession(AndroidVersion.GINGERBREAD));
			}
			
			CyclicIterator<Session> sessions = new CyclicIterator<Session>(imageSessions);

			NextAppResponse[] appsResponse = fetcher.getNextAppIds(Main.commentIndex);
			if (appsResponse.length == 0) {
				log.info("Reached the end of collection. Reseting the Start index to 0.");
				
				Main.commentIndex = 0;
				appsResponse = fetcher.getNextAppIds(Main.commentIndex);
			}

			for (NextAppResponse nextAppResponse : appsResponse) {
				set.add(executorService.submit(new CommentThread(sessions.next(), fetcher, nextAppResponse.app.id, nextAppResponse.app.appId)));
			}

		} catch (UserUnavailableException e) {
			e.printStackTrace();
		} catch (ConnectivityException e) {
			e.printStackTrace();
		}
		
		return set;
	}

	private void execute() {
		Set<Future<?>> globalSet = new HashSet<Future<?>>();
		
		if (category == true) {
			globalSet.addAll(createCategoryThread());
		}

		if (comment == true) {
			globalSet.addAll(createCommentThread());
		}

		if (image == true) {
			globalSet.addAll(createImageThread());
		}

		if (publisher == true) {
			globalSet.addAll(createPublisherThread());
		}

		if (packages == true) {
			globalSet.addAll(createPackageThread());
		}
		
		try {
			// Execute all threads. 
			for (Future<?> future : globalSet) {
				future.get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void resolveArgs(Options options, String args[]) {
		CommandLineParser parser = new GnuParser();

		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption('c')) {
				this.category = true;
			}

			if (line.hasOption("com")) {
				this.comment = true;
			}

			if (line.hasOption('i')) {
				this.image = true;
			}

			if (line.hasOption('l')) {
				this.latest = line.getOptionValue('l') != null ? line.getOptionValue('l').equals("true") : false;
			}

			if (line.hasOption("pname")) {
				this.packages = true;
				this.packageFile = line.getOptionValue("pname");
			}

			if (line.hasOption("pub")) {
				this.publisher = true;
				this.publisherFile = line.getOptionValue("pub");
			}

		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		execute();
	}

	public static void main(String args[]) {
		Options options = new CLI().getOptions();

		if (args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("android-marketplace-crawler", options);

		} else {
			new Main().resolveArgs(options, args);
		}

	}
}
