package txtuml.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// TODO needs optimization
class CollectionBuilder<T extends ModelClass> implements Iterable<T> {
	private final List<T> list = new LinkedList<>();

	CollectionBuilder<T> append(T object) {
		list.add(object);
		return this;
	}

	CollectionBuilder<T> append(Collection<T> objects) {
		for (T object : objects) {
			list.add(object);
		}
		return this;
	}

	public Iterator<T> iterator() {
		return list.iterator();
	}
}