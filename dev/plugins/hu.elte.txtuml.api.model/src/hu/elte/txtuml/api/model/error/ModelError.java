package hu.elte.txtuml.api.model.error;

/**
 * Base type of runtime errors raised by the txtUML modeling API. They are Java
 * errors because they represent fatal errors in the model that make it
 * impossible to continue the execution of the model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class ModelError extends Error {

	public ModelError() {
	}

	public ModelError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ModelError(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelError(String message) {
		super(message);
	}

	public ModelError(Throwable cause) {
		super(cause);
	}

}
