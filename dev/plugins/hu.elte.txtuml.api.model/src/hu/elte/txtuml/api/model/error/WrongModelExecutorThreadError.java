package hu.elte.txtuml.api.model.error;

/**
 * Thrown if the caller thread is not the model executor thread which owns the accessed
 * model element.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class WrongModelExecutorThreadError extends Error {

	public WrongModelExecutorThreadError() {
		super("The current thread is not the model executor thread which owns the accessed model element.");
	}

}
