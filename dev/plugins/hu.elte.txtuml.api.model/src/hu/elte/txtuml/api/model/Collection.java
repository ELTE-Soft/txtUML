package hu.elte.txtuml.api.model;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

import hu.elte.txtuml.api.model.GeneralCollection.NonUnique;
import hu.elte.txtuml.api.model.GeneralCollection.Unordered;

// TODO document
public abstract class Collection<E, C extends Collection<E, C>>
		extends AbstractGeneralCollection<E, C> implements @External Unordered<E>, @External NonUnique<E> {

	@ExternalBody
	protected Collection() {
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
		java.util.Collection<E> backend = getBackend();
		if (backend instanceof Multiset) {
			return ((Multiset<E>) backend).count(element);
		}
		int count = 0;
		for (E e : backend) {
			if (e.equals(element)) {
				++count;
			}
		}
		return count;
	}

	@ExternalBody
	@Override
	public final Any<E> unbound() {
		return asAny();
	}

	@Override
	java.util.Collection<E> createBackend(Consumer<Builder<E>> backendBuilder) {
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
