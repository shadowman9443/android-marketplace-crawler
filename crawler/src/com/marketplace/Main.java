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

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.marketplace.io.Fetcher;
import com.marketplace.io.Session;
import com.marketplace.io.SessionManager;
import com.marketplace.service.CategoryThread;
import com.marketplace.service.PackageThread;
import com.marketplace.service.PublisherThread;

/**
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

	/**
	 * Start <code>Thread</code> for each Android Marketplace Category.
	 */
	private void startCategoryThreads() {
		log.info("Creating threads for fetching apps via category.");
		Fetcher fetcher = new Fetcher();

		ExecutorService executorService = Executors.newFixedThreadPool(22);
		Iterator<String> categories = Category.getAllCategories();

		try {
			SessionManager sessionManager = new SessionManager();
			for (Session session : sessionManager.getSessions()) {
				while (categories.hasNext()) {
					executorService.submit(new CategoryThread(session, fetcher, categories.next(), this.latest));
				}
			}

			executorService.shutdown();
			executorService.awaitTermination(25, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startPackageThread() {
		log.info("Creating threads for fetching apps via packages.");

		if (this.packageFile == null) {
			System.err.println("Please pass the location of file using the args '-pnamef'");
			System.exit(1);
		}

		Fetcher fetcher = new Fetcher();
		Iterator<String> packageNames = fetcher.readFile(this.packageFile).iterator();
		ExecutorService executorService = Executors.newFixedThreadPool(22);

		try {
			SessionManager sessionManager = new SessionManager();
			Session[] sessions = sessionManager.getSessions();
			for (int i = sessions.length; i > 0; i--) {
				while (packageNames.hasNext()) {
					executorService.submit(new PackageThread(sessions[i - 1], fetcher, packageNames.next()));
				}
			}
			executorService.shutdown();
			executorService.awaitTermination(25, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startPublisherThread() {
		log.info("Creating threads for fetching apps via publishers.");

		if (this.publisherFile == null) {
			System.err.println("Please pass the location of file using the args '-pubf'");
			System.exit(1);
		}

		Fetcher fetcher = new Fetcher();
		Iterator<String> pubNames = fetcher.readFile(this.publisherFile).iterator();
		ExecutorService executorService = Executors.newFixedThreadPool(22);

		try {
			SessionManager sessionManager = new SessionManager();
			Session[] sessions = sessionManager.getSessions();

			for (int i = sessions.length; i > 0; i--) {
				while (pubNames.hasNext()) {
					executorService.submit(new PublisherThread(sessions[i - 1], fetcher, pubNames.next()));
				}
			}
			executorService.shutdown();
			executorService.awaitTermination(25, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startImageThread() {
		// TODO: Implement this
	}

	private void startCommentThread() {
		// TODO: Implement this
	}

	private void execute() {
		if (category == true) {
			startCategoryThreads();
		}

		if (comment == true) {
			startCommentThread();
		}

		if (image == true) {
			startImageThread();
		}

		if (publisher == true) {
			startPublisherThread();
		}

		if (packages == true) {
			startPackageThread();
		}
	}

	private void retrieveArgs(Options options, String args[]) {
		System.out.println("Program Start - Executing \n");
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
			}

			if (line.hasOption("pnamef")) {
				this.packageFile = line.getOptionValue("pnamef");
			}

			if (line.hasOption("pub")) {
				this.publisher = true;
			}

			if (line.hasOption("pubf")) {
				this.publisherFile = line.getOptionValue("pubf");
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
			new Main().retrieveArgs(options, args);
		}

	}
}
