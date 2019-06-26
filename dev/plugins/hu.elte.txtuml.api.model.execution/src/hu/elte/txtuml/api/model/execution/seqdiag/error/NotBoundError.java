package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Occurs when the expression not binded.
 */
@SequenceDiagramRelated

public class NotBoundError extends SequenceDiagramProblem {

	public NotBoundError() {
		super("Some of the expressions are not bound", ErrorLevel.ERROR);
	}

}