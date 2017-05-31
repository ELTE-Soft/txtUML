package hu.elte.txtuml.api.model;

import java.util.Iterator;
import java.util.Spliterator;

//TODO document
public abstract class GeneralCollection<E> implements Iterable<E> {

	GeneralCollection() {
	}

	public abstract <C2 extends GeneralCollection<E>> C2 as(Class<C2> collectionType);

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
	}

	public interface Unordered<E> extends Ordering<Unordered<E>> {
	}

	public interface Unique<E> extends Uniqueness<Unique<E>> {
	}

	public interface NonUnique<E> extends Uniqueness<NonUnique<E>> {
		int countOf(E element);
	}

	// PRIVATE INTERFACES

	private interface Ordering<O extends Ordering<O>> {
	}

	private interface Uniqueness<U extends Uniqueness<U>> {
	}

}
