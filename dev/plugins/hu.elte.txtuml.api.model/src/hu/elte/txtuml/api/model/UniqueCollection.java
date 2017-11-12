package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableSet;

import hu.elte.txtuml.api.model.GeneralCollection.Unique;
import hu.elte.txtuml.api.model.GeneralCollection.Unordered;
import hu.elte.txtuml.api.model.error.CollectionCopyError;
import hu.elte.txtuml.api.model.utils.Collections;

// TODO document
public abstract class UniqueCollection<E, C extends UniqueCollection<E, C>> extends
		AbstractGeneralCollection<E, ImmutableSet<E>, C> implements @External Unordered<E>, @External Unique<E> {

	@ExternalBody
	protected UniqueCollection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	UniqueCollection(ImmutableSet<E> backend) {
		super(backend);
	}

	@ExternalBody
	@Override
	@SuppressWarnings("unchecked")
	public final <C2 extends GeneralCollection<? super E>, C3 extends GeneralCollection<?>> C2 as(
			Class<C3> collectionType) {
		if (Collections.isUnordered(collectionType)) {
			return (C2) asUnsafe(collectionType);
		} else {
			throw new CollectionCopyError(getClass(), collectionType);
		}
	}

	@ExternalBody
	@Override
	public final UniqueAny<E> unbound() {
		return new UniqueAny<>(getBackend());
	}

	@External
	@Override
	public final boolean isOrdered() { // to become final
		return Unordered.super.isOrdered();
	}

	@External
	@Override
	public final boolean isUnique() { // to become final
		return Unique.super.isUnique();
	}

	@Override
	ImmutableSet<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableSet.Builder<E> builder = ImmutableSet.builder();
		backendBuilder.accept(builder::add);
		return builder.build();
	}

	@ExternalBody
	@Override
	public final String toString() {
		return "{" + super.toString() + "}";
	}

}
