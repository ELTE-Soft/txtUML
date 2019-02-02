package hu.elte.txtuml.validation.sequencediagram;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

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
	protected ASTNode getNodeToMark(ASTNode source) {
		if (source instanceof MethodInvocation) {
			return ((MethodInvocation) source).getName();
		}

		return super.getNodeToMark(source);
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
