package hu.elte.txtuml.validation.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class TriggerOnInitialTransition extends ValidationErrorBase {

	public TriggerOnInitialTransition(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INITIAL_TRANSITION_WITH_TRIGGER.ordinal();
	}

	@Override
	public String getMessage() {
		return "Triggers are not allowed on transitions from an initial state or a choice pseudostate";
	}

}
