package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Runtime;

/**
 * txtUML models has to be run in a txtUML runtime context, that is, a
 * {@link hu.elte.txtuml.api.model.Runtime} object must manage them. This
 * interface provides access to that {@code Runtime} instance.
 * <p>
 * <b>Note:</b> all model executor threads (Java threads on which a model runs)
 * must implement this interface. Model executors has to suffice this
 * requirement.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface RuntimeContext {

	/**
	 * The runtime instance that owns this object.
	 */
	Runtime getRuntime();

}
