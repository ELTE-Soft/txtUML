package hu.elte.txtuml.validation.sequencediagram;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

import hu.elte.txtuml.validation.common.AbstractValidationError;
import hu.elte.txtuml.validation.common.SourceInfo;

/**
 * Base class for all sequence diagram description validation errors.
 */
public abstract class SequenceValidationError extends AbstractValidationError {

	public SequenceValidationError(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
		if (node instanceof MethodInvocation) {
			SimpleName name = ((MethodInvocation) node).getName();
			this.sourceStart = name.getStartPosition();
			this.sourceEnd = name.getStartPosition() + name.getLength() - 1;
		} else {
			this.sourceStart = node.getStartPosition();
			this.sourceEnd = node.getStartPosition() + node.getLength() - 1;
		}

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
