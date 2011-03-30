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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <code>CyclicIterator</code> allows one to iterate over elements in a infinite
 * loop.
 * 
 * @version 1.0
 * @referenced Stackoverflow
 */
public class CyclicIterator<T> implements Iterator<T> {

	private Iterator<T> iterator;
	private final List<T> list;

	public CyclicIterator(List<T> list) {
		this.list = list;
		this.iterator = list.iterator();
	}

	@Override
	public boolean hasNext() {
		return !list.isEmpty();
	}

	@Override
	public T next() {
		T nextElement;

		if (!hasNext()) {
			throw new NoSuchElementException();
		} else if (iterator.hasNext()) {
			nextElement = iterator.next();
		} else {
			iterator = list.iterator();
			nextElement = iterator.next();
		}

		return nextElement;

	}

	@Override
	public void remove() {
		iterator.remove();
	}

}