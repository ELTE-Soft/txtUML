package hu.elte.txtuml.api.model.error;

/**
 * Thrown if the caller thread is not a model executor thread. Note that an
 * existing thread can become a model executor thread or it can loose this
 * property during its lifetime.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class NotModelExecutorThreadError extends Error {

	public NotModelExecutorThreadError() {
		super("Missing runtime context. The current thread is not a model executor thread.");
	}

}
