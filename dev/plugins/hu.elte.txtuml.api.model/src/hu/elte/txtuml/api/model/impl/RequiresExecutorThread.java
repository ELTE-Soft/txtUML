package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * Base interface for runtime wrappers that require to be owned by a model
 * executor thread and therefore has an associated thread engine.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface RequiresExecutorThread extends ImplRelated {

	/**
	 * The engine of the thread that owns the wrapped element.
	 */
	ExecutorThread getThread();

}
