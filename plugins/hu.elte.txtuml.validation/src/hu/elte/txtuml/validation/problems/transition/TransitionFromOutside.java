package hu.elte.txtuml.validation.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class TransitionFromOutside extends ValidationErrorBase {

	public TransitionFromOutside(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.TRANSITION_FROM_OUTSIDE.ordinal();
	}

	@Override
	public String getMessage() {
		return "Transition source should be in the same class as the transition itself.";
	}

}
