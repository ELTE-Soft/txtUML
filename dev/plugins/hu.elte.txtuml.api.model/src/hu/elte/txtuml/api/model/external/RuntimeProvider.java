package hu.elte.txtuml.api.model.external;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * This is a helper type, only intended to be used inside this package.
 * <p>
 * Provides an {@link ImplRelated} instance on which the
 * {@link ImplRelated#getRuntimeOf(hu.elte.txtuml.api.model.ImplRelated.RequiresRuntime)
 * getRuntimeOf} method can be called.
 */
enum RuntimeProvider implements ImplRelated {
	INSTANCE
}
