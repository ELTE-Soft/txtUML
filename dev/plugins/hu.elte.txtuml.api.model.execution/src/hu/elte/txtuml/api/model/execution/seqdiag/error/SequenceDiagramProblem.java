package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * General sequence diagram error.
 */
@SequenceDiagramRelated
public class SequenceDiagramProblem {

	private final ErrorLevel level;
	private final String message;

	public SequenceDiagramProblem(String concreteError, ErrorLevel level) {
		this.message = concreteError;
		this.level = level;
	}

	public ErrorLevel getErrorLevel() {
		return level;
	}

	public String getMessage() {
		return message;
	}

}
