package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultiset;

import hu.elte.txtuml.api.model.GeneralCollection.NonUnique;
import hu.elte.txtuml.api.model.GeneralCollection.Unordered;

// TODO document
public abstract class Collection<E, C extends Collection<E, C>>
		extends AbstractGeneralCollection<E, ImmutableMultiset<E>, C>
		implements @External Unordered<E>, @External NonUnique<E> {

	@ExternalBody
	protected Collection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	Collection(ImmutableMultiset<E> backend) {
		super(backend);
	}

	@ExternalBody
	@Override
	@SuppressWarnings("unchecked")
	public final <C2 extends GeneralCollection<? super E>, C3 extends GeneralCollection<?>> C2 as(
			Class<C3> collectionType) {
		if (Unordered.class.isAssignableFrom(collectionType) && NonUnique.class.isAssignableFrom(collectionType)) {
			return (C2) asUnsafe(collectionType);
		} else {
			// TODO exception handling
			throw new Error();
		}
	}

	@ExternalBody
	@Override
	public final int countOf(E element) {
		return getBackend().count(element);
	}

	@ExternalBody
	@Override
	public final Any<E> unbound() {
		return new Any<>(getBackend());
	}

	@Override
	ImmutableMultiset<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableMultiset.Builder<E> builder = ImmutableMultiset.builder();
		backendBuilder.accept(builder::add);
		return builder.build();
	}

	@ExternalBody
	@Override
	public final String toString() {
		return "[" + super.toString() + "]";
	}

}
