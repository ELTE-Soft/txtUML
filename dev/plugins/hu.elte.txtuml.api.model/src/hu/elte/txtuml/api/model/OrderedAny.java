package hu.elte.txtuml.api.model;

import com.google.common.collect.ImmutableList;

/**
 * Ordered, non-unique, 0..* txtUML API collection and as such, an immutable
 * class. For detailed information about the txtUML API collections, see the
 * documentation of {@link GeneralCollection}.
 * 
 * <p>
 * <b>Represents:</b> ordered, non-unique, 0..* collection of primitives or
 * objects
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Use the {@link Action#collectIn(Class, Object...) } method to instantiate
 * this class and fill the new collection with elements. After that, the methods
 * of this class can be used to manage it.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed (instantiate it through the provided API
 * methods)</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @param <E>
 *            the type of the elements contained in this collection
 * 
 * @see Action#collectIn(Class, Object...)
 */
public final class OrderedAny<E> extends OrderedCollection<E, OrderedAny<E>> {

	/**
	 * As this class should only be instantiated by the txtUML API, its
	 * constructors are package private.
	 */
	OrderedAny() {
	}

	/**
	 * This constructor sets the backend of this collection without any
	 * multiplicity checks.
	 */
	OrderedAny(ImmutableList<E> backend) {
		super(backend);
	}

	@Override
	final int getLowerBoundPackagePrivate() {
		return 0;
	}

	@Override
	final int getUpperBoundPackagePrivate() {
		return GeneralCollection.INFINITE_BOUND;
	}

}
