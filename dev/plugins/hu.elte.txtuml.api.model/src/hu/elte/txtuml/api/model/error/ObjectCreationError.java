package hu.elte.txtuml.api.model.error;

/**
 * Thrown if a model object is to be created with action code operation but the
 * creation fails with the given parameters.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class ObjectCreationError extends ModelError {

	public ObjectCreationError(Throwable cause) {
		super("The creation of a model object was impossible with the given arguments.", cause);
	}

}
