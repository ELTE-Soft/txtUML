package hu.elte.txtuml.api.stdlib.mcollections;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.external.ExternalClass;

/**
 * Base type of <b>mutable</b> txtUML collections.
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @param <T>
 *            the type of objects to be contained in this collection
 */
public interface MCollection<T> extends ExternalClass, Iterable<T> {
	
	/**
	 * Checks if this mutable collection is empty.
	 * 
	 * @return {@code true} if this collection is empty; {@code false} otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns the number of elements in this mutable collection.
	 * 
	 * @return the size of this collection
	 */
	int count();

	/**
	 * Checks whether a certain model object is in this mutable collection.
	 * 
	 * @param element
	 *            the model object to check
	 * @return {@code true} if this collection contains the specified
	 *         {@code object}; {@code false} otherwise
	 */
	boolean contains(Object element);

	/**
	 * Selects an element of this mutable collection. Nor randomness, nor any iteration
	 * order is guaranteed, this method is allowed to return the same object
	 * each time it is called on the same collection.
	 * 
	 * @return an element of this collection, {@code null} if the collection is
	 *         empty
	 */
	T selectAny();

	/**
	 * Adds a new element to this mutable collection.
	 *
	 * @param element
	 *            the object to be included in the collection
	 * @return true if this collection is changed after the call
	 */
	boolean add(T element);

	/**
	 * Adds all the given elements to this mutable collection.
	 *
	 * @param elements
	 *            the elements which are to be included in this collection
	 * @return true if this collection is changed after the call
	 * @throws NullPointerException
	 *             if {@code objects} is {@code null}
	 */
	boolean addAll(Iterable<T> elements);

	/**
	 * Removes the given element from this mutable collection.
	 *
	 * @param element
	 *            the object <i>not</i> to be included in this collection
	 * @return true if this collection is changed after the call
	 */
	boolean remove(Object element);

	/**
	 * Removes all elements from this mutable collection.
	 */
	void clear();
	
}
