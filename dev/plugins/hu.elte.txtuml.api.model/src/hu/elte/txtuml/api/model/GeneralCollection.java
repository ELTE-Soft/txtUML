package hu.elte.txtuml.api.model;

import java.util.Iterator;
import java.util.Spliterator;

//TODO document
public abstract class GeneralCollection<E> implements Iterable<E>, GeneralCollectionProperties {

	public static final int INFINITE_BOUND = -1;

	GeneralCollection() {
	}

	public abstract <C2 extends GeneralCollection<? super E>,
		C3 extends GeneralCollection<?>> C2 as(Class<C3> collectionType);

	public abstract boolean isEmpty();

	public abstract int size();

	public abstract E one();

	public abstract boolean contains(Object element);

	public abstract GeneralCollection<E> add(E element);

	public abstract GeneralCollection<E> remove(Object element);

	public abstract GeneralCollection<E> unbound();

	@Override
	public abstract Iterator<E> iterator();

	/**
	 * This method <b>must not be used in the model</b>.
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public final Spliterator<E> spliterator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method <b>must not be used in the model</b>.
	 */
	@Override
	protected final void finalize() throws Throwable {
		// Nothing to do.
	}

	// ORDERING AND UNIQUENESS

	public interface Ordered<E> extends Ordering<Ordered<E>> {
		Ordered<E> add(int index, E element);

		E get(int index);

		Ordered<E> remove(int index);

		@Override
		default boolean isOrdered() {
			return true;
		}
	}

	public interface Unordered<E> extends Ordering<Unordered<E>> {
		@Override
		default boolean isOrdered() {
			return false;
		}
	}

	public interface Unique<E> extends Uniqueness<Unique<E>> {
		@Override
		default boolean isUnique() {
			return true;
		}
	}

	public interface NonUnique<E> extends Uniqueness<NonUnique<E>> {
		int countOf(E element);

		@Override
		default boolean isUnique() {
			return false;
		}
	}

	// PRIVATE INTERFACES

	private interface Ordering<O extends Ordering<O>> extends GeneralCollectionProperties {
	}

	private interface Uniqueness<U extends Uniqueness<U>> extends GeneralCollectionProperties {
	}

}
