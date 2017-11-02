package hu.elte.txtuml.api.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;
import hu.elte.txtuml.api.model.GeneralCollection.Unique;

//TODO document
public abstract class OrderedUniqueCollection<E, C extends OrderedUniqueCollection<E, C>>
		extends AbstractOrderedCollection<E, C> implements @External Ordered<E>, @External Unique<E> {

	@ExternalBody
	protected OrderedUniqueCollection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	OrderedUniqueCollection(ImmutableList<E> backend) {
		super(backend);
	}

	@ExternalBody
	@Override
	@SuppressWarnings("unchecked")
	public final <C2 extends GeneralCollection<? super E>, C3 extends GeneralCollection<?>> C2 as(
			Class<C3> collectionType) {
		return (C2) asUnsafe(collectionType);
	}

	@ExternalBody
	@Override
	public final OrderedUniqueAny<E> unbound() {
		return new OrderedUniqueAny<>(getBackend());
	}

	@Override
	ImmutableList<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableList.Builder<E> builder = ImmutableList.builder();
		Set<E> tmp = new HashSet<>();
		backendBuilder.accept(e -> {
			if (!tmp.contains(e)) {
				tmp.add(e);
				builder.add(e);
			}
		});
		return builder.build();
	}

	@ExternalBody
	@Override
	public final String toString() {
		return "<" + super.toString() + ">";
	}

}
