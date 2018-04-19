package hu.elte.txtuml.api.model.external;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * This is a helper type, only intended to be used inside this package.
 * <p>
 * Provides an {@link ImplRelated} instance on which the
 * {@link ImplRelated#getRuntimeOf(hu.elte.txtuml.api.model.ImplRelated.RequiresRuntime)
 * getRuntimeOf} method can be called.
 * <p>
 * <i>Reason of existence:</i> As the {@link ImplRelated} interface shows that
 * its subtype is related to the implementation of model executors and should
 * not be used in the model or in external libraries, therefore the public types
 * of this package should not extend or implement it.
 */
enum RuntimeProvider implements ImplRelated {
	INSTANCE
}
