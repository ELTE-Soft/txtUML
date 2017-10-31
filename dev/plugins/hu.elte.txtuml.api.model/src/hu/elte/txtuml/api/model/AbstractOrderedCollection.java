package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;

//TODO document
abstract class AbstractOrderedCollection<E, C extends AbstractOrderedCollection<E, C>>
		extends AbstractGeneralCollection<E, C> implements @External Ordered<E> {

	@ExternalBody
	protected AbstractOrderedCollection() {
	}

	@ExternalBody
	@Override
	public final C add(int index, E element) {
		return createSameTyped(builder -> {
			int i = 0;
			for (E e : getBackend()) {
				if (index == i) {
					builder.add(element);
				}
				builder.add(e);
				++i;
			}
		});
	}

	@ExternalBody
	@Override
	public final E get(int index) {
		// TODO review exception handling
		java.util.Collection<E> backend = getBackend();
		if (backend instanceof java.util.List) {
			return ((java.util.List<E>) backend).get(index);
		}
		int i = 0;
		for (E e : backend) {
			if (index != i) {
				return e;
			}
			++i;
		}
		throw new IndexOutOfBoundsException("Size: " + backend.size() + " Index: " + index);
	}

	@ExternalBody
	@Override
	public final C remove(int index) {
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
	java.util.List<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableList.Builder<E> builder = ImmutableList.builder();
		backendBuilder.accept(builder::add);
		return builder.build();
	}

}
