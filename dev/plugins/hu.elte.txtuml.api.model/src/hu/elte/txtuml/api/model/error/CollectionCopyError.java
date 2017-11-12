package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;

/**
 * Thrown when a txtUML API collection is to be copied by the
 * {@link GeneralCollection#as} action but the copy fails because of the
 * ordering and uniqueness restrictions of the action.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class CollectionCopyError extends ModelError {

	@SuppressWarnings("rawtypes")
	public CollectionCopyError(Class<? extends GeneralCollection> toBeCopied,
			Class<? extends GeneralCollection> copyTo) {
		super("Collection " + toBeCopied.getName() + " cannot be copied as " + copyTo.getName() + ".");
	}

}
