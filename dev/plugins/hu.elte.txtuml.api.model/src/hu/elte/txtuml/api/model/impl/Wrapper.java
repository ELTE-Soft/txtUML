package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * An immutable wrapper of an object which can be used as a base interface for
 * other wrappers which may extend the wrapped object with additional
 * capabilities.
 * <p>
 * Naming convention: wrappers that have a state (that are mutable) are called
 * 'Runtime'.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
@FunctionalInterface
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

}
