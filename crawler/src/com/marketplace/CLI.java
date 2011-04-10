package com.marketplace;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * A simple Command Line Interface options manager.
 * 
 * @author raunak
 * @version 1.0
 */
public class CLI {

	/**
	 * Holds many <code>Option</code> objects
	 */
	private Options options = new Options();

	/**
	 * Constructs a <code>CLI</code> object
	 */
	public CLI() {
		addOption(new Option("c", "category", false, "fetch app(s) for all categories"));
		addOption(new Option("com", "comment", false, "fetch comment(s) for all app(s)"));
		addOption(new Option("i", "image", false, "fetch image for all app(s)"));
		addOption(new Option("pub", "publisher", false, "fetch app(s) by publisher(s)"));
		addOption(new Option("pname", "package", false, "fetch app(s) by package(s)"));
		addOption(new Option("l", "latest", true, "fetch latest app(s)"));
		addOption(new Option("pubf", "publisherfile", true, "location of file containing publisher(s)"));
		addOption(new Option("pnamef", "packagefile", true, "location of file containing package name(s)"));
	}

	/**
	 * Adds an <code>Option</code> to a list.
	 * 
	 * @param option
	 *            the <code>Option</code> or the argument that you wish to add.
	 */
	private void addOption(Option option) {
		this.options.addOption(option);
	}

	/**
	 * Get the available options for command line interface
	 * 
	 * @return list of supported arg(s)
	 */
	public Options getOptions() {
		return options;
	}
}
