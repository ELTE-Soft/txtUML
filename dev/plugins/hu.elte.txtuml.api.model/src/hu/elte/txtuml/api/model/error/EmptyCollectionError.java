package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.error.ModelError;

/**
 * Thrown when an element is to be taken from a txtUML API collection but the
 * collection is empty.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 * 
 * @see GeneralCollection#one
 */
@SuppressWarnings("serial")
public class EmptyCollectionError extends ModelError {

}
