package hu.elte.txtuml.validation.sequencediagram;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.common.AbstractValidationError;
import hu.elte.txtuml.validation.common.SourceInfo;

/**
 * Base class for all sequence diagram description validation errors.
 */
public abstract class SequenceValidationError extends AbstractValidationError {

	public SequenceValidationError(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public String getMarkerType() {
		return SequenceDiagramCompilationParticipant.MARKER_TYPE;
	}

	@Override
	public String toString() {
		return getType() + " (" + getMessage() + ")";
	}

}
