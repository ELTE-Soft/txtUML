package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;

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
		return getBackend().get(index);
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
	ImmutableList<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableList.Builder<E> builder = ImmutableList.builder();
		backendBuilder.accept(builder::add);
		return builder.build();
	}

}
