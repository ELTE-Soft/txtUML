package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Thrown by certain methods of the {@link SequenceDiagramExecutor} interface
 * which cannot be called after that specific model executor has been started.
 * Indicates that the sequence diagram executor did start before the call of
 * that method.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@SuppressWarnings("serial")
@SequenceDiagramRelated
public class LockedSeqDiagExecutorException extends LockedModelExecutorException {

	public LockedSeqDiagExecutorException() {
		super("The accessed sequence diagram executor is locked. This method cannot be called.");
	}

}