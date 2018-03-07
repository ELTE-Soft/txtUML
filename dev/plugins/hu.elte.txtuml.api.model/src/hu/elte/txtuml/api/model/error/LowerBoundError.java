package hu.elte.txtuml.api.model.error;

import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.utils.Collections;

/**
 * Thrown when the lower bound of a txtUML API collection is offended.
 * <p>
 * See the documentation of {@link GeneralCollection} for detailed information
 * about the txtUML API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class LowerBoundError extends MultiplicityError {

	public LowerBoundError(@SuppressWarnings("rawtypes") Class<? extends GeneralCollection> type) {
		super(type, Collections.getLowerBound(type));
	}

	public LowerBoundError(GeneralCollection<?> instance) {
		super(instance.getClass(), instance.getLowerBound());
	}

}
