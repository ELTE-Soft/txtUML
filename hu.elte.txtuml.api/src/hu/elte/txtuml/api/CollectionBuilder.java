package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.JavaCollectionOfMany;

import java.util.Iterator;

class CollectionBuilder<T extends ModelClass> implements Iterable<T> {

	private final JavaCollectionOfMany<T> collection = JavaCollectionOfMany
			.create();

	CollectionBuilder<T> append(T object) {
		collection.add(object);
		return this;
	}

	CollectionBuilder<T> append(Collection<T> objects) {
		for (T object : objects) {
			collection.add(object);
		}
		return this;
	}

	JavaCollectionOfMany<T> getJavaCollection() {
		return collection;
	}

	public Iterator<T> iterator() {
		return collection.iterator();
	}
}