package hu.elte.txtuml.validation.model;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.common.AbstractValidationError;
import hu.elte.txtuml.validation.common.SourceInfo;

/**
 * Base class for all JtxtUML model validation errors.
 */
public abstract class ModelValidationError extends AbstractValidationError {

	public ModelValidationError(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public String getMarkerType() {
		return JtxtUMLModelCompilationParticipant.JTXTUML_MODEL_MARKER_TYPE;
	}

	@Override
	public String toString() {
		return getType() + " (" + getMessage() + ")";
	}

}
