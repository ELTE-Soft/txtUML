package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.blocks.ParameterizedCondition;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

/**
 * Base interface for immutable collections of {@link ModelClass} objects.
 * 
 * <p>
 * <b>Represents:</b> collection of model objects
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * After getting an object which implements this interface, call its methods
 * anywhere from the model where action code or condition evaluation is written.
 * <p>
 * See the documentation of {@link Model} for details about the action language
 * and condition evaluations in the model.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Define subtype:</i> disallowed, use its predefined subclasses instead,
 * like {@link Collection.Empty} or {@link AssociationEnd}</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 * @see Collection.Empty
 * @see AssociationEnd
 */
public interface Collection<T extends ModelClass> extends Iterable<T>,
		ModelElement {

	/**
	 * Checks if this collection is empty.
	 * 
	 * @return <code>true</code> if this collection is empty; <code>false</code>
	 *         otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the size of this collection
	 */
	int count();

	/**
	 * Checks whether a certain model object is in this collection.
	 * 
	 * @param object
	 *            the model object to check
	 * @return <code>true</code> if this collection contains the specified
	 *         <code>object</code>; <code>false</code> otherwise
	 */
	boolean contains(ModelClass object);

	/**
	 * Selects an element of this collection. Nor randomness, nor any iteration
	 * order is guaranteed, this method is allowed to return the same object
	 * each time it is called on the same collection.
	 * 
	 * @return an element of this collection, <code>null</code> if the
	 *         collection is empty
	 */
	T selectAny();

	/**
	 * Selects all elements of this collection for which the specified condition
	 * holds.
	 * 
	 * @param cond
	 *            a condition to filter the elements of this collection
	 * @return a new collection containing the selected elements
	 * @throws NullPointerException
	 *             if <code>cond</code> is <code>null</code>
	 */
	Collection<T> selectAll(ParameterizedCondition<T> cond);

	/**
	 * Creates a new collection which contains all the elements of this
	 * collection and also the specified object.
	 *
	 * @param object
	 *            the object to be included in the result collection
	 * @return a new collection containing the desired elements
	 */
	Collection<T> add(T object);

	/**
	 * Creates a new collection which contains all the elements of this
	 * collection and also the elements of the specified collection.
	 *
	 * @param objects
	 *            the other collection which's elements are to be included in
	 *            the result
	 * @return a new collection containing the desired elements
	 * @throws NullPointerException
	 *             if <code>objects</code> is <code>null</code>
	 */
	Collection<T> addAll(Collection<T> objects);

	/**
	 * Creates a new collection which contains all the elements of this
	 * collection without the specified object (if it was included in the
	 * collection).
	 *
	 * @param object
	 *            the object <i>not</i> to be included in the result collection
	 * @return a new collection containing the desired elements
	 */
	Collection<T> remove(T object);

	/**
	 * This method <b>must not be used in the model</b>.
	 */
	@Override
	default Spliterator<T> spliterator() {
		return Iterable.super.spliterator();
	}

	/**
	 * A default implementation for an empty collection.
	 * 
	 * <p>
	 * <b>Represents:</b> empty collection
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> allowed</li>
	 * <li><i>Define subtype:</i> disallowed</li>
	 * </ul>
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 * @see Collection
	 */
	public static final class Empty<T extends ModelClass> implements
			Collection<T> {
		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				@Override
				public boolean hasNext() {
					return false;
				}

				@Override
				public T next() {
					throw new NoSuchElementException();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public int count() {
			return 0;
		}

		@Override
		public boolean contains(ModelClass object) {
			return false;
		}

		@Override
		public T selectAny() {
			return null;
		}

		@Override
		public Collection<T> selectAll(ParameterizedCondition<T> cond) {
			return this;
		}

		@Override
		public Collection<T> add(T object) {
			return new MaybeOneBase<T>(object);
		}

		@Override
		public Collection<T> addAll(Collection<T> objects) {
			return new ManyBase<T>(objects);
		}

		@Override
		public Collection<T> remove(ModelClass object) {
			return this;
		}
	}
}
