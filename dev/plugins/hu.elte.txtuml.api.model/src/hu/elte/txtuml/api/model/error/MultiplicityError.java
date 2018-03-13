package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;

/**
 * Thrown when the lower or upper bound of a txtUML API collection is offended.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class MultiplicityError extends ModelError {

	public MultiplicityError(@SuppressWarnings("rawtypes") Class<? extends GeneralCollection> type, int size) {
		super("Collection type " + type.getName() + " cannot be created with " + size + " elements.");
	}

}
