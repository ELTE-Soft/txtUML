package hu.elte.txtuml.validation.model;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.validation.common.AbstractValidationError;
import hu.elte.txtuml.validation.common.SourceInfo;

/**
 * Base class for all JtxtUML model validation errors.
 */
public abstract class ModelValidationError extends AbstractValidationError {

	public ModelValidationError(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
		if (node instanceof MethodDeclaration || node instanceof TypeDeclaration) {
			SimpleName name;
			if (node instanceof MethodDeclaration) {
				name = ((MethodDeclaration) node).getName();
			} else {
				name = ((TypeDeclaration) node).getName();
			}
			this.sourceStart = name.getStartPosition();
			this.sourceEnd = name.getStartPosition() + name.getLength();
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
