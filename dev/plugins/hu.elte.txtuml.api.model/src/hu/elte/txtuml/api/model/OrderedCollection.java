package hu.elte.txtuml.api.model;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.NonUnique;
import hu.elte.txtuml.api.model.GeneralCollection.Ordered;

//TODO document
public abstract class OrderedCollection<E, C extends OrderedCollection<E, C>> extends AbstractOrderedCollection<E, C>
		implements @External Ordered<E>, @External NonUnique<E> {

	@ExternalBody
	protected OrderedCollection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	OrderedCollection(ImmutableList<E> backend) {
		super(backend);
	}

	@ExternalBody
	@Override
	@SuppressWarnings("unchecked")
	public final <C2 extends GeneralCollection<? super E>, C3 extends GeneralCollection<?>> C2 as(
			Class<C3> collectionType) {
		if (NonUnique.class.isAssignableFrom(collectionType)) {
			return (C2) asUnsafe(collectionType);
		} else {
			// TODO exception handling
			throw new Error();
		}
	}

	@ExternalBody
	@Override
	public final int countOf(E element) {
		int count = 0;
		for (E e : getBackend()) {
			if (e.equals(element)) {
				++count;
			}
		}
		return count;
	}

	@ExternalBody
	@Override
	public final OrderedAny<E> unbound() {
		return new OrderedAny<>(getBackend());
	}

	@ExternalBody
	@Override
	public final String toString() {
		return "(" + super.toString() + ")";
	}

}
