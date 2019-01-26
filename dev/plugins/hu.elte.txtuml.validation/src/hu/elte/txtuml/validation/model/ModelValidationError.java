package hu.elte.txtuml.validation.model;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

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
	protected ASTNode getMarkedASTNode(ASTNode source) {
		if (source instanceof MethodDeclaration || source instanceof AbstractTypeDeclaration) {
			SimpleName name;
			if (source instanceof MethodDeclaration) {
				name = ((MethodDeclaration) source).getName();
			} else {
				name = ((AbstractTypeDeclaration) source).getName();
			}
			return name;
		} else {
			return source;
		}
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
