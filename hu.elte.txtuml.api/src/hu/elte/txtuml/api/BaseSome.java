package hu.elte.txtuml.api;

/**
 * Base class of association ends having a multiplicity of 1..*.
 * <p>
 * Inherits its implementation from <code>BaseMany</code>.
 * <p>
 * Directly unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class BaseSome<T extends ModelClass> extends BaseMany<T> {

	@Override
	boolean checkLowerBound() {
		return getSize() > 0;
	}

}
