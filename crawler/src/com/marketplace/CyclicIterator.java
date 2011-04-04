package com.marketplace;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <code>CyclicIterator</code> allows one to iterate over elements in a infinite
 * loop.
 * 
 * @author unknown
 * @version 1.0
 * @see Stackoverflow
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