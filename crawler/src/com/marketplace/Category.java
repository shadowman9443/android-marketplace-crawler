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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of App categories that the Android Marketplace supports.
 * 
 * @author raunak
 * @version 1.0
 * 
 */
public class Category {
	/**
	 * <Strong>Lifestyle</strong> Category.
	 */
	public static final String LIFESTYLE = "LIFESTYLE";

	/**
	 * <Strong>Multimedia</strong> Category.
	 */
	public static final String MULTIMEDIA = "MULTIMEDIA";

	/**
	 * <Strong>News</strong> Category.
	 */
	public static final String NEWS = "NEWS";

	/**
	 * <Strong>Productivity</strong> Category.
	 */
	public static final String PRODUCTIVITY = "PRODUCTIVITY";

	/**
	 * <Strong>Reference</strong> Category.
	 */
	public static final String REFERENCE = "REFERENCE";

	/**
	 * <Strong>Shopping</strong> Category.
	 */
	public static final String SHOPPING = "SHOPPING";

	/**
	 * <Strong>Social</strong> Category.
	 */
	public static final String SOCIAL = "SOCIAL";

	/**
	 * <Strong>Sports</strong> Category.
	 */
	public static final String SPORTS = "SPORTS";

	/**
	 * <Strong>Themes</strong> Category.
	 */
	public static final String THEMES = "THEMES";

	/**
	 * <Strong>Tools</strong> Category.
	 */
	public static final String TOOLS = "TOOLS";

	/**
	 * <Strong>Travel</strong> Category.
	 */
	public static final String TRAVEL = "TRAVEL";

	/**
	 * <Strong>Demo</strong> Category.
	 */
	public static final String DEMO = "DEMO";

	/**
	 * <Strong>Library</strong> Category.
	 */
	public static final String LIBRARIES = "LIBRARIES";

	/**
	 * <Strong>Arcade</strong> Category.
	 */
	public static final String ARCADE = "ARCADE";

	/**
	 * <Strong>Brain</strong> Category.
	 */
	public static final String BRAIN = "BRAIN";

	/**
	 * <Strong>Cards</strong> Category.
	 */
	public static final String CARDS = "CARDS";

	/**
	 * <Strong>Casual</strong> Category.
	 */
	public static final String CASUAL = "CASUAL";

	/**
	 * <Strong>Comics</strong> Category.
	 */
	public static final String COMICS = "COMICS";

	/**
	 * <Strong>Communication</strong> Category.
	 */
	public static final String COMMUNICATION = "COMMUNICATION";

	/**
	 * <Strong>Entairtainment</strong> Category.
	 */
	public static final String ENTERTAINMENT = "ENTERTAINMENT";

	/**
	 * <Strong>Finance</strong> Category.
	 */
	public static final String FINANCE = "FINANCE";

	/**
	 * <Strong>Health</strong> Category.
	 */
	public static final String HEALTH = "HEALTH";

	/**
	 * Gets all the categories that are available
	 * 
	 * @return <code>Iterator</code><<code>String</code>>
	 */
	public static Iterator<String> getAllCategories() {
		List<String> categories = new ArrayList<String>(22);

		categories.add(ARCADE);
		categories.add(DEMO);
		categories.add(ENTERTAINMENT);
		categories.add(FINANCE);
		categories.add(HEALTH);
		categories.add(LIBRARIES);
		categories.add(LIFESTYLE);
		categories.add(MULTIMEDIA);
		categories.add(NEWS);
		categories.add(REFERENCE);
		categories.add(THEMES);
		categories.add(TRAVEL);
		categories.add(BRAIN);
		categories.add(CARDS);
		categories.add(CASUAL);
		categories.add(COMICS);
		categories.add(COMMUNICATION);
		categories.add(PRODUCTIVITY);
		categories.add(SHOPPING);
		categories.add(SOCIAL);
		categories.add(SPORTS);
		categories.add(TOOLS);

		return categories.iterator();
	}
}
