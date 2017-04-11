package hu.elte.txtuml.validation.model;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.common.SourceInfo;
import hu.elte.txtuml.validation.common.ValidationProblem;

/**
 * Base class for all JtxtUML model problems.
 */
public abstract class ModelValidationError extends ValidationProblem.Error {

	public ModelValidationError(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public abstract int getID();
	
	@Override
	public String getMarkerType() {
		return JtxtUMLCompilationParticipant.JTXTUML_MARKER_TYPE;
	}

}
