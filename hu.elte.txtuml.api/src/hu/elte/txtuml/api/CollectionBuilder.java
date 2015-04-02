package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.JavaCollectionOfMany;

import java.util.Iterator;

class CollectionBuilder<T extends ModelClass> implements Iterable<T> {

	private JavaCollectionOfMany<T> collection = JavaCollectionOfMany.create();

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
		JavaCollectionOfMany<T> coll = collection;
		collection = null;
		return coll;
	}

	@Override
	public Iterator<T> iterator() {
		return collection.iterator();
	}
}