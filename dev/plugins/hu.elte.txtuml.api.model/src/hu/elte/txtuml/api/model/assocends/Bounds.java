package hu.elte.txtuml.api.model.assocends;

/**
 * Association ends are bounded: they have a lower bound and an upper bound.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface Bounds {

	/**
	 * Returns the lower bound of this multiplicity.
	 * 
	 * @return a non-negative integer that is the lower bound of this
	 *         multiplicity
	 */
	int lowerBound();

	/**
	 * Returns the upper bound of this multiplicity, -1 means no upper bound.
	 * 
	 * @return a non-negative integer that is the upper bound of this
	 *         multiplicity or -1 if there is no upper bound
	 */
	int upperBound();

	/**
	 * Checks whether the given actual size conforms to the lower bound.
	 */
	default boolean checkLowerBound(int actualSize) {
		return actualSize >= lowerBound();
	}

	/**
	 * Checks whether the given actual size conforms to the upper bound.
	 */
	default boolean checkUpperBound(int actualSize) {
		int bound = upperBound();
		return bound == -1 || actualSize <= bound;
	}

}
