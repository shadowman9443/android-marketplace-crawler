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
package com.raunak.exceptions;

/**
 * <code>UserNotAvailableException</code> can be thrown when all the user name
 * for accessing the Google Marketplace are in use.
 * 
 * @author raunak
 * @version 1.0
 */
public class UserUnavailableException extends Exception {

	private static final long serialVersionUID = -889501132294477815L;

	public UserUnavailableException() {
		super("All username are in use at the moment");
	}
}
