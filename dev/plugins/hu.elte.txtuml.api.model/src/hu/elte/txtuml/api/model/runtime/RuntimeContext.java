package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Runtime;

public interface RuntimeContext {

	/**
	 * The runtime instance that owns this object.
	 */
	Runtime getRuntime();

}
