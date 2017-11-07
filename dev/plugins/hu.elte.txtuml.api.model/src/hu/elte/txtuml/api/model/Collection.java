package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultiset;

import hu.elte.txtuml.api.model.GeneralCollection.NonUnique;
import hu.elte.txtuml.api.model.GeneralCollection.Unordered;
import hu.elte.txtuml.api.model.error.CollectionCopyError;
import hu.elte.txtuml.api.model.utils.Collections;

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
		if (Collections.isUnordered(collectionType) && Collections.isNonUnique(collectionType)) {
			return (C2) asUnsafe(collectionType);
		} else {
			throw new CollectionCopyError();
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

	@External
	@Override
	public final boolean isOrdered() { // to become final
		return Unordered.super.isOrdered();
	}

	@External
	@Override
	public final boolean isUnique() { // to become final
		return NonUnique.super.isUnique();
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
