package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * General sequence diagram error.
 */
@SequenceDiagramRelated
public class SequenceDiagramProblem extends RuntimeException {

	private static final long serialVersionUID = 3750870627402653606L;

	private final ErrorLevel level;

	public SequenceDiagramProblem(String concreteError, ErrorLevel level) {
		super(concreteError);
		this.level = level;
	}

	public ErrorLevel getErrorLevel() {
		return level;
	}

}
