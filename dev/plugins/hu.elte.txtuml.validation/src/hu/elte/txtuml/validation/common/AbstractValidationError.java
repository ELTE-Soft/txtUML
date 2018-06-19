package hu.elte.txtuml.validation.common;

import org.eclipse.jdt.core.dom.ASTNode;

public abstract class AbstractValidationError extends AbstractValidationProblem {

	public AbstractValidationError(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public boolean isError() {
		return true;
	}

	public abstract int getID();

	public abstract IValidationErrorType getType();

	public abstract String getMarkerType();

}
