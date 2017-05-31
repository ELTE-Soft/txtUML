package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;

//TODO document
abstract class AbstractOrderedCollection<E, C extends AbstractOrderedCollection<E, C>>
		extends AbstractGeneralCollection<E, java.util.List<E>, C> implements Ordered<E> {

	protected AbstractOrderedCollection() {
	}

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

	@Override
	public final E get(int index) {
		return getBackend().get(index);
	}

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
	final java.util.List<E> getUninitializedBackend() {
		return null; // TODO uninitialized collection
	}

	@Override
	java.util.List<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableList.Builder<E> builder = ImmutableList.builder();
		backendBuilder.accept(builder::add);
		return builder.build();
	}

}
