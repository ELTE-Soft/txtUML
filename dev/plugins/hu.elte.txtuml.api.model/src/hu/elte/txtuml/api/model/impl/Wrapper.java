package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * An immutable wrapper of an object which can be used as a base interface for
 * other wrappers which may extend the wrapped object with additional
 * capabilities.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface Wrapper<W> extends ImplRelated {

	/**
	 * Gets the wrapped object.
	 */
	W getWrapped();

	/**
	 * A shorthand operation for
	 * {@link #getWrapped()}&#x2e;{@link Object#getClass() getClass()}.
	 */
	@SuppressWarnings("unchecked")
	default Class<W> getTypeOfWrapped() {
		return (Class<W>) getWrapped().getClass();
	}

	/**
	 * Returns a short string representation of the wrapped object.
	 */
	default String getStringRepresentation() {
		return toString();
	}

	/**
	 * A wrapper should have a custom string representation based on the wrapped
	 * element.
	 */
	@Override
	String toString();

}
