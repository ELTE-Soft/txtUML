package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.external.Collections;

/**
 * Thrown when the upper bound of a txtUML API collection is offended.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class UpperBoundError extends MultiplicityError {

	public UpperBoundError(@SuppressWarnings("rawtypes") Class<? extends GeneralCollection> type) {
		super(type, Collections.getUpperBound(type));
	}

	public UpperBoundError(GeneralCollection<?> instance) {
		super(instance.getClass(), instance.getUpperBound());
	}

}
