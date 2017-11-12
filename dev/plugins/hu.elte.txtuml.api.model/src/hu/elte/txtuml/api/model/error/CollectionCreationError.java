package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;

/**
 * Thrown when a txtUML API collection of a specified type is to be created but
 * the creation fails due to some runtime Java errors. Most often caused by
 * instantiating custom collections which do not have a zero parameter
 * constructor.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class CollectionCreationError extends ModelError {

	public CollectionCreationError(Throwable cause) {
		super(cause);
	}

}
