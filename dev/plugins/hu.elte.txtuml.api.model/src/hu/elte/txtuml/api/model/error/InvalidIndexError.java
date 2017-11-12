package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;

/**
 * Thrown if an element of an ordered collection is to be accessed through its
 * index but the index is invalid.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class InvalidIndexError extends ModelError {

	public InvalidIndexError(int actual) {
		super("Wrong index: " + actual);
	}

	public InvalidIndexError(int size, int actual) {
		super("Wrong index: " + actual + ". Size of collection: " + size);
	}

}
