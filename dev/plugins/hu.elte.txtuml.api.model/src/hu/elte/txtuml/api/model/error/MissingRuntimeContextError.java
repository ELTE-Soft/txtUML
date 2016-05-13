package hu.elte.txtuml.api.model.error;

/**
 * Thrown if the code should be run in a runtime context (that is, on a thread
 * that implements {@link hu.elte.txtuml.api.model.runtime.RuntimeContext}), but
 * is not.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class MissingRuntimeContextError extends Error {

	public MissingRuntimeContextError() {
		super("Missing runtime context. The current thread is not a RuntimeContext.");
	}

}
