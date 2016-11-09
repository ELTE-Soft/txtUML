package hu.elte.txtuml.api.stdlib.mcollections;

import java.util.Iterator;

abstract class AbstractMCollection<T, B extends java.util.Collection<T>>
		implements MCollection<T> {

	final B backend;

	protected AbstractMCollection(B backend) {
		this.backend = backend;
	}

	@Override
	public Iterator<T> iterator() {
		return backend.iterator();
	}

	@Override
	public boolean isEmpty() {
		return count() == 0;
	}

	@Override
	public int count() {
		return backend.size();
	}

	@Override
	public boolean contains(Object element) {
		return backend.contains(element);
	}

	@Override
	public T selectAny() {
		Iterator<T> it = backend.iterator();
		if (it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}

	@Override
	public boolean add(T element) {
		return backend.add(element);
	}

	@Override
	public boolean addAll(Iterable<T> objects) {
		boolean changed = false;
		for (T e : objects) {
			changed |= backend.add(e);
		}
		return changed;
	}

	@Override
	public boolean remove(Object element) {
		return backend.remove(element);
	}
	
	@Override
	public void clear() {
		backend.clear();
	}

}
