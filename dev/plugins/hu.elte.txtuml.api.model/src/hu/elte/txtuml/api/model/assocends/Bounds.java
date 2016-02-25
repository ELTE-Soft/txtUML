package hu.elte.txtuml.api.model.assocends;

public interface Bounds {

	/**
	 * Returns the lower bound of this multiplicity.
	 * 
	 * @return a non-negative integer that is the lower bound of this
	 *         multiplicity
	 */
	int lowerBound();

	/**
	 * Returns the upper bound of this multiplicity. -1 means no upper bound.
	 * 
	 * @return a non-negative integer that is the upper bound of this
	 *         multiplicity or -1 if there is no upper bound
	 */
	int upperBound();

	default boolean checkLowerBound(int actualSize) {
		return actualSize >= lowerBound();
	}

	default boolean checkUpperBound(int actualSize) {
		int bound = upperBound();
		return bound == -1 || actualSize <= bound;
	}

}
