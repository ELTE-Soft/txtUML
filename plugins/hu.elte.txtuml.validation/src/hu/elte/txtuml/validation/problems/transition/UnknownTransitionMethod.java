package hu.elte.txtuml.validation.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class UnknownTransitionMethod extends ValidationErrorBase {

	public UnknownTransitionMethod(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.UNKNOWN_TRANSITION_METHOD.ordinal();
	}

	@Override
	public String getMessage() {
		return "Only an effect method can be in a transition";
	}

}
