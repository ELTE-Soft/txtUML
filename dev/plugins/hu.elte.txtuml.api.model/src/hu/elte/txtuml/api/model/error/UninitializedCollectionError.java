package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;

/**
 * Thrown when a txtUML API collection collection is accessed which is not
 * properly initialized. Only possible if the collection instance is <i>not</i>
 * created in a valid way, that is, with the appropriate
 * {@link hu.elte.txtuml.api.model.Action Action} methods or methods of the
 * collection types. Most often caused by instantiating custom collections
 * directly with the call of their constructors which is not allowed.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class UninitializedCollectionError extends ModelError {
}
