package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public enum ExecMode {
	/**
	 * Only the given messages may appear.
	 */
	STRICT,
	/**
	 * 'Extra' messages may appear.
	 */
	LENIENT
}
