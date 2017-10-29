package hu.elte.txtuml.api.model;

/**
 * Unordered, unique, 1..* txtUML API collection and as such, an immutable
 * class. For detailed information about the txtUML API collections, see the
 * documentation of {@link GeneralCollection}.
 * 
 * <p>
 * <b>Represents:</b> unordered, unique, 1..* collection of primitives or
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
@Min(1)
public final class UniqueOneToAny<E> extends UniqueCollection<E, UniqueOneToAny<E>> {

	/**
	 * As this class should only be instantiated by the txtUML API, its sole
	 * constructor is package private.
	 */
	UniqueOneToAny() {
	}

	@Override
	final int getLowerBoundPackagePrivate() {
		return 1;
	}

	@Override
	final int getUpperBoundPackagePrivate() {
		return GeneralCollection.INFINITE_BOUND;
	}

}
