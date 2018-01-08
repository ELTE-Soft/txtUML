package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * Base for runtime wrappers of JtxtUML model elements.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface ModelElementRuntime<T> extends Wrapper<T>, RequiresExecutorThread, ImplRelated {

	/**
	 * The model runtime instance that owns this object.
	 */
	default ModelRuntime getModelRuntime() {
		return getThread().getModelRuntime();
	}

}
