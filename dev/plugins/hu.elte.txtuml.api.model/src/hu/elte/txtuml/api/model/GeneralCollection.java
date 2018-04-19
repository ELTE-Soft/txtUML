package hu.elte.txtuml.api.model;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Abstract base class for JtxtUML API collections in the model. Should not be
 * used in the model (only its subtypes) as this type does not specify the
 * multiplicity, ordering or uniqueness of actual the collection instance.
 * 
 * <p>
 * <b>Represents:</b> collection
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Should not be used directly.
 * <p>
 * Use its first level subtypes ({@link Collection}, {@link OrderedCollection},
 * {@link UniqueCollection}, or {@link OrderedUniqueCollection}) to define new
 * collections. Use its second level subtypes (subclasses of the above listed
 * four) inside the model to contain objects and primitives.
 * <p>
 * All JtxtUML API collections are immutable and therefore all their methods are
 * thread-safe. However, they cannot be created by calling their constructors
 * (not even the user-defined collections). The appropriate actions
 * ({@link Action#collect} and {@link Action#collectIn}) should be used instead
 * which also initialize the new collections properly (otherwise, a model error
 * may occur when using the collection instance).
 * <p>
 * From outside the model, the methods of the
 * {@link hu.elte.txtuml.api.model.external.Collections} class can be used to
 * analyze collection types.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed, use its subtypes {@link Collection},
 * {@link OrderedCollection}, {@link UniqueCollection} or
 * {@link OrderedUniqueCollection}</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @param <E>
 *            the type of elements contained in this collection
 * 
 * @see Collection
 * @see OrderedCollection
 * @see UniqueCollection
 * @see OrderedUniqueCollection
 * @see Any
 * @see OrderedAny
 * @see UniqueAny
 * @see OrderedUniqueAny
 * @see OneToAny
 * @see OrderedOneToAny
 * @see UniqueOneToAny
 * @see OrderedUniqueOneToAny
 * @see One
 * @see ZeroToOne
 */
public abstract class GeneralCollection<E> implements @External Iterable<E>, @External GeneralCollectionProperties {

	/*
	 * GeneralCollection is a class and not an interface to ensure that it
	 * cannot be implemented directly by users, only by extending the given
	 * subclasses (Collection, OrderedCollection, etc.).
	 */

	/**
	 * In case of the upper bound of a collection's multiplicity, this value
	 * represents (positive) infinity.
	 */
	public static final int INFINITE_BOUND = -1;

	GeneralCollection() {
	}

	/**
	 * Returns a collection of the given type that contains the same elements as
	 * this. If either the lower bound or upper bound of the specified type
	 * would be offended with this copy, a model error is raised. If this
	 * collection is unordered or non-unique and the given type is ordered or
	 * unique (respectively), that also results in a model error.
	 * <p>
	 * If the given type is the same as the type of this, this collection
	 * is returned.
	 * <p>
	 * <i>Reason for ordering and uniqueness restriction:</i> this method is
	 * meant to perform a trivial copy without any further actions, that is, the
	 * result should contain the same elements in the same order (if any).
	 * Ordered collections can safely be converted to unordered ones and also
	 * unique collections to non-unique ones. The opposite directions, however,
	 * are problematic:
	 * <ul>
	 * <li><i>unordered {@code ->} ordered conversion:</i> the order in which
	 * the result would contain the elements cannot be reasonably
	 * specified;</li>
	 * <li><i>non-unique {@code ->} unique conversion:</i> the size of the
	 * result could be different as additional instances of the same element
	 * would have to be removed.</li>
	 * </ul>
	 * 
	 * @param collectionType
	 *            the type of the returned collection
	 * @result a copy of this (or this) of the specified type
	 * 
	 */

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
	 * reference we store in "ints3", that is, we cannot put an Object in this
	 * collection which would cause a ClassCastException when accessed through
	 * the "ints" reference).
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

	/**
	 * Tests whether this collection is empty. That is, whether it contains no
	 * elements.
	 * 
	 * @return {@code true} if this is empty; {@code false} otherwise
	 */
	@ExternalBody
	public abstract boolean isEmpty();

	/**
	 * Returns the size of this collection, that is, the number of elements
	 * contained.
	 * <p>
	 * <b>Non-unique collections:</b> every element is counted as many times as
	 * they are contained.
	 * 
	 * @return the size of this
	 */
	@ExternalBody
	public abstract int size();

	/**
	 * Returns an element of this collection; if this collection is empty, it
	 * results in a model error.
	 * <p>
	 * <b>Ordered collections:</b> the first element is returned.
	 * <p>
	 * <b>Unordered collections:</b> an arbitrary element is returned.
	 * 
	 * @return an element of this collection
	 */
	@ExternalBody
	public abstract E one();

	/**
	 * Tests whether the specified element is contained in this collection.
	 * <p>
	 * <b>Non-unique collections:</b> tests whether the element is contained
	 * <i>at least once</i>
	 * <p>
	 * Two objects that are equal are considered to be the same element.
	 * 
	 * @return {@code true} of the specified element is contained in this
	 *         collection (at least once); {@code false} otherwise
	 */
	@ExternalBody
	public abstract boolean contains(Object element);

	/**
	 * Returns a collection of the same type as this which contains all the
	 * elements of this and also the specified element.
	 * <p>
	 * <b>Unique collections:</b> if the element is already contained, the
	 * action cannot be performed and therefore this collection will be
	 * returned.
	 * <p>
	 * <b>Non-unique collections:</b> if the element is already contained, it
	 * will be contained one time more.
	 * <p>
	 * <b>Ordered collections:</b> the element is added as the last one.
	 * <p>
	 * Two objects that are equal are considered to be the same element.
	 * 
	 * @return a copy of this (or this) which contains the specified element one
	 *         time more (if allowed)
	 */
	@ExternalBody
	public abstract GeneralCollection<E> add(E element);

	/**
	 * Returns a collection of the same type as this which contains all the
	 * elements of this except the specified element.
	 * <p>
	 * If the element is not contained, the action cannot be performed and
	 * therefore this collection will be returned.
	 * <p>
	 * <b>Non-unique collections:</b> if the element is contained more than
	 * once, it will be contained in the result one time less.
	 * <p>
	 * <b>Non-unique, ordered collections:</b> if the element is contained more
	 * than once, its first appearance will be removed.
	 * <p>
	 * Two objects that are equal are considered to be the same element.
	 * 
	 * @return a copy of this (or this) which does not contain the specified
	 *         element (or contain it one time less)
	 */
	@ExternalBody
	public abstract GeneralCollection<E> remove(Object element);

	/**
	 * Returns an unbounded copy of this collection, that is, a collection which
	 * has the same ordering and uniqueness properties and contains the same
	 * elements but has a 0..* multiplicity.
	 * <p>
	 * Naturally, this means that the returned collection will be one of the
	 * following, based on the ordering and uniqueness of this collection:
	 * <ul>
	 * <li>Any (unordered, non-unique)</li>
	 * <li>OrderedAny (ordered, non-unique)</li>
	 * <li>UniqueAny (unordered, unique)</li>
	 * <li>OrderedUniqueAny (ordered, unique)</li>
	 * </ul>
	 * 
	 * @return an unbounded copy of this collection
	 */
	@ExternalBody
	public abstract GeneralCollection<E> unbound();

	@ExternalBody
	@Override
	public abstract boolean equals(Object obj);

	/**
	 * This method <b>must not be used in the model</b>.
	 */
	@External
	@Override
	public abstract int hashCode();

	/**
	 * This method <b>must not be used <i>directly</i> in the model</b>, only
	 * through the use of enhanced for-loops.
	 */
	@External
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
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	// ORDERING AND UNIQUENESS

	/**
	 * An external base type of ordered txtUML API Collections. The
	 * {@link #isOrdered()} method of subtypes must return {@code true}.
	 * <p>
	 * Ordered collections contain the elements in a fixed order, that is, their
	 * elements can be referenced with an index. This order does not have to fit
	 * the natural ordering or any other kind of ordering of the elements. It is
	 * based on the order in which the elements were put into the collection.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @param <E>
	 *            type of the elements in the collection
	 */
	@External
	public interface Ordered<E> extends Ordering<Ordered<E>> {

		/**
		 * Returns the element in this collection at the specified index.
		 * <p>
		 * If the given index is invalid, a model error is raised. The index is
		 * invalid if it is <i>not</i> between 0 (inclusive) and the size of
		 * this collection (exclusive).
		 * 
		 * @return the element at the specified index
		 */
		@ExternalBody
		E get(int index);

		/**
		 * Returns a collection of the same type as this which contains all the
		 * elements of this with the specified element added to the given index.
		 * <p>
		 * Index of elements after the given index will be increased by 1.
		 * <p>
		 * If the given index is invalid, a model error is raised. The index is
		 * invalid if it is <i>not</i> between 0 (inclusive) and the size of
		 * this collection (inclusive).
		 * <p>
		 * Note that in the case of this method, the size of this collection is
		 * also a valid index. In this case, this method adds the element to the
		 * end of this collection, so it has the same result as the
		 * {@link GeneralCollection#add(Object)} method.
		 * 
		 * @return a copy of this which contains the element at the specified
		 *         index
		 */
		@ExternalBody
		Ordered<E> add(int index, E element);

		/**
		 * Returns a collection of the same type as this which contains all the
		 * elements of this except the one at the specified index.
		 * <p>
		 * Index of elements after the given index will be decreased by 1.
		 * <p>
		 * If the given index is invalid, a model error is raised. The index is
		 * invalid if it is <i>not</i> between 0 (inclusive) and the size of
		 * this collection (exclusive).
		 * 
		 * @return a copy of this which does not contain the element at the
		 *         specified index
		 */
		@ExternalBody
		Ordered<E> remove(int index);

		@External
		@Override
		default boolean isOrdered() {
			return true;
		}
	}

	/**
	 * An external base type of unordered txtUML API Collections. The
	 * {@link #isOrdered()} method of subtypes must return {@code false}.
	 * <p>
	 * Ordered collections contain the elements in a fixed order, that is, their
	 * elements can be referenced with an index. This order does not have to fit
	 * the natural ordering or any other kind of ordering of the elements. It is
	 * based on the order in which the elements were put into the collection.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @param <E>
	 *            type of the elements in the collection
	 */
	@External
	public interface Unordered<E> extends Ordering<Unordered<E>> {
		@External
		@Override
		default boolean isOrdered() {
			return false;
		}
	}

	/**
	 * An external base type of unique txtUML API Collections. The
	 * {@link #isUnique()} method of subtypes must return {@code true}.
	 * <p>
	 * Unique collections cannot contain multiple elements which are equal.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @param <E>
	 *            type of the elements in the collection
	 */
	@External
	public interface Unique<E> extends Uniqueness<Unique<E>> {
		@External
		@Override
		default boolean isUnique() {
			return true;
		}
	}

	/**
	 * An external base type of non-unique txtUML API Collections. The
	 * {@link #isUnique()} method of subtypes must return {@code false}.
	 * <p>
	 * Unique collections cannot contain multiple elements which are equal.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @param <E>
	 *            type of the elements in the collection
	 */
	@External
	public interface NonUnique<E> extends Uniqueness<NonUnique<E>> {

		/**
		 * Returns the number of times the specified element is contained in
		 * this collection.
		 * <p>
		 * Two objects that are equal are considered to be the same element.
		 * 
		 * @return how many times the specified element is contained
		 */
		@ExternalBody
		int countOf(E element);

		@External
		@Override
		default boolean isUnique() {
			return false;
		}
	}

	// PRIVATE INTERFACES

	/**
	 * This interface exists to make sure that no collection can be the subtype
	 * of {@link Ordered} and {@link Unordered} at the same time. It would be a
	 * compile time error because of the restrictions of Java on type
	 * parameters.
	 */
	private interface Ordering<O extends Ordering<O>> extends GeneralCollectionProperties {
	}

	/**
	 * This interface exists to make sure that no collection can be the subtype
	 * of {@link Unique} and {@link NonUnique} at the same time. It would be a
	 * compile time error because of the restrictions of Java on type
	 * parameters.
	 */
	private interface Uniqueness<U extends Uniqueness<U>> extends GeneralCollectionProperties {
	}

}
