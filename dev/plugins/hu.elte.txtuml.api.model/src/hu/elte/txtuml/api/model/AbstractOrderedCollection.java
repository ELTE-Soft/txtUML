package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;
import hu.elte.txtuml.api.model.error.InvalidIndexError;

//TODO document
abstract class AbstractOrderedCollection<E, C extends AbstractOrderedCollection<E, C>>
		extends AbstractGeneralCollection<E, ImmutableList<E>, C> implements @External Ordered<E> {

	@ExternalBody
	protected AbstractOrderedCollection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	AbstractOrderedCollection(ImmutableList<E> backend) {
		super(backend);
	}

	@ExternalBody
	@Override
	public final C add(int index, E element) {
		if (index < 0) {
			throw new InvalidIndexError(index);
		}
		if (index > size()) { // index == size is allowed here
			throw new InvalidIndexError(size(), index);
		}

		return createSameTyped(builder -> {
			int i = 0;
			for (E e : getBackend()) {
				if (index == i) {
					builder.add(element);
				}
				builder.add(e);
				++i;
			}
			if (index == i) { // if index == size
				builder.add(element);
			}
		});
	}

	@ExternalBody
	@Override
	public final E get(int index) {
		if (index < 0) {
			throw new InvalidIndexError(index);
		}
		if (index >= size()) {
			throw new InvalidIndexError(size(), index);
		}

		return getBackend().get(index);
	}

	@ExternalBody
	@Override
	public final C remove(int index) {
		if (index < 0) {
			throw new InvalidIndexError(index);
		}
		if (index >= size()) {
			throw new InvalidIndexError(size(), index);
		}

		return createSameTyped(builder -> {
			int i = 0;
			for (E e : getBackend()) {
				if (index != i) {
					builder.add(e);
				}
				++i;
			}
		});
	}

	@Override
	ImmutableList<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableList.Builder<E> builder = ImmutableList.builder();
		backendBuilder.accept(builder::add);
		return builder.build();
	}

}
