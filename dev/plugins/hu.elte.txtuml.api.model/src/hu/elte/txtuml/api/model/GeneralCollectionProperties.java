package hu.elte.txtuml.api.model;

/**
 * Base interface for the {@link GeneralCollection} class and some interfaces
 * which represent different kinds of txtUML API collections. The methods of
 * this interface provide information about the basic properties of a txtUML API
 * collection, like ordering, uniqueness and bounds.
 * <p>
 * As txtUML API collections are immutable, the methods of this interface has to
 * return the same result for the same object whenever they are called.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @see GeneralCollection
 * @see GeneralCollection.Ordered
 * @see GeneralCollection.Unordered
 * @see GeneralCollection.Unique
 * @see GeneralCollection.NonUnique
 */
interface GeneralCollectionProperties {
	/**
	 * Tells whether this txtUML API collection is ordered or not.
	 * <p>
	 * Ordered collections contain the elements in a fixed order, that is, their
	 * elements can be referenced with an index. This order does not have to fit
	 * the natural ordering or any other kind of ordering of the elements. In
	 * most cases, it is based on the order in which the elements were put into
	 * the collection.
	 */
	@External
	boolean isOrdered();

	/**
	 * Tells whether this txtUML API collection is a unique collection or not.
	 * <p>
	 * Unique collections cannot contain multiple elements which are equal.
	 */
	@External
	boolean isUnique();

	/**
	 * Returns the lower bound of this collection, that is, the minimum number
	 * of elements it may contain. It can be 0 or any positive integer.
	 */
	@External
	int getLowerBound();

	/**
	 * Returns the upper bound of this collection, that is, the maximum number
	 * of elements it may contain. It can be 0, any positive integer or equal to
	 * {@link GeneralCollection#INFINITE_BOUND}, which means that this
	 * collection has no upper bound.
	 */
	@External
	int getUpperBound();
}
