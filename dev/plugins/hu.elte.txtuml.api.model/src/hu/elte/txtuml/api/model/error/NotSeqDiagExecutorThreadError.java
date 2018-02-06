package hu.elte.txtuml.api.model.error;

/**
 * Thrown if the caller thread is not a sequence diagram executor thread. Note
 * that an existing thread can become a sequence diagram executor thread or it
 * can loose this property during its lifetime.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class NotSeqDiagExecutorThreadError extends Error {

	public NotSeqDiagExecutorThreadError() {
		super("The current thread is not a sequence diagram executor thread.");
	}

}
