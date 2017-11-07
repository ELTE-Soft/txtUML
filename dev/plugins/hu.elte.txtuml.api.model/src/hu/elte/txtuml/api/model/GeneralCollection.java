package hu.elte.txtuml.api.model;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

//TODO document
public abstract class GeneralCollection<E> implements @External Iterable<E>, @External GeneralCollectionProperties {

	/*
	 * GeneralCollection is a class and not an interface to ensure that it
	 * cannot be implemented directly by users, only by extending the given
	 * subclasses (Collection, OrderedCollection, etc.).
	 */

	public static final int INFINITE_BOUND = -1;

	GeneralCollection() {
	}

	/*
	 * The "as" method was initially declared as:
	 * 
	 * public abstract <C2 extends GeneralCollection<E>> C2 as( Class<C2>
	 * collectionType);
	 * 
	 * This version might seem better than the current but it is not. After
	 * writing
	 * 
	 * Any<Integer> ints = Action.collection(10);
	 * 
	 * The old version always raised an unchecked warning on the following line,
	 * saying 'Type safety: The expression of type One needs unchecked
	 * conversion to conform to One<Integer>'.
	 * 
	 * One<Integer> ints2 = ints.as(One.class);
	 * 
	 * The old version also allowed a user to write:
	 * 
	 * One<String> ints3 = ints.as(One.class);
	 * 
	 * Which would have run, but would have caused a ClassCastException later
	 * when iterating over the elements. This is a compile error now, unless the
	 * element type to which the user wishes to change to is a super type of the
	 * original (for example, if "Object" was written instead of "String"
	 * above). This case is completely safe because these collections are
	 * immutable (we cannot change the original collection instance through the
	 * reference we store in "ints3").
	 * 
	 * The current version might also raise a ClassCastException like in the
	 * case of "ints4" below, but only at the place of the call and not at an
	 * unspecified later time. Therefore, it is straightforward how to fix it.
	 * 
	 * Any<Integer> ints4 = ints.as(One.class);
	 * 
	 * Note: a perfect declaration of "as" cannot be written due to the
	 * properties of Java.
	 */
	@ExternalBody
	public abstract <C2 extends GeneralCollection<? super E>, C3 extends GeneralCollection<?>> C2 as(
			Class<C3> collectionType);

	@ExternalBody
	public abstract boolean isEmpty();

	@ExternalBody
	public abstract int size();

	@ExternalBody
	public abstract E one();

	@ExternalBody
	public abstract boolean contains(Object element);

	@ExternalBody
	public abstract GeneralCollection<E> add(E element);

	@ExternalBody
	public abstract GeneralCollection<E> remove(Object element);

	@ExternalBody
	public abstract GeneralCollection<E> unbound();

	@ExternalBody
	@Override
	public abstract Iterator<E> iterator();

	/**
	 * This method <b>must not be used in the model</b>.
	 */
	@External
	@Override
	public final void forEach(Consumer<? super E> action) {
		Iterable.super.forEach(action);
	}

	/**
	 * This method <b>must not be used in the model</b>.
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@External
	@Override
	public final Spliterator<E> spliterator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method <b>must not be used in the model</b>.
	 */
	@External
	@Override
	protected final void finalize() throws Throwable {
		// Nothing to do.
	}

	/**
	 * This method <b>must not be used in the model</b>.
	 */
	@External
	@Override
	public abstract int hashCode();

	@ExternalBody
	@Override
	public abstract boolean equals(Object obj);

	// ORDERING AND UNIQUENESS

	@External
	public interface Ordered<E> extends Ordering<Ordered<E>> {
		Ordered<E> add(int index, E element);

		E get(int index);

		Ordered<E> remove(int index);

		@Override
		default boolean isOrdered() {
			return true;
		}
	}

	@External
	public interface Unordered<E> extends Ordering<Unordered<E>> {
		@Override
		default boolean isOrdered() {
			return false;
		}
	}

	@External
	public interface Unique<E> extends Uniqueness<Unique<E>> {
		@Override
		default boolean isUnique() {
			return true;
		}
	}

	@External
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
