package hu.elte.txtuml.api.model.runtime.collections;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import hu.elte.txtuml.api.model.Collection;

/**
 * A mutable builder for an immutable collection.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of items contained in the collection
 * @param <C>
 *            the type of the immutable collection
 */
interface Builder<T, C extends Collection<T>> {

	Builder<T, C> add(T element);

	default Builder<T, C> addAll(Iterable<? extends T> elements) {
		elements.forEach(this::add);
		return this;
	}

	default Builder<T, C> addAll(Iterator<? extends T> it) {
		it.forEachRemaining(this::add);
		return this;
	}

	/**
	 * May only be called <b>once</b> to create the prepared immutable
	 * collection.
	 */
	C build();

	// create method

	static <T, C extends Collection<T>, B extends java.util.Collection<T>> Builder<T, C> create(
			Supplier<B> backendCollectionCreator, Function<B, C> immutableCollectionCreator) {
		return new Builder<T, C>() {
			private B backendCollection = backendCollectionCreator.get();

			@Override
			public Builder<T, C> add(T element) {
				backendCollection.add(element);
				return this;
			}

			@Override
			public C build() {
				C ret = immutableCollectionCreator.apply(backendCollection);
				backendCollection = null;
				return ret;
			}
		};
	}

}
