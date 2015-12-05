package hu.elte.txtuml.validation.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class MissingTransitionTrigger extends ValidationErrorBase {

	public MissingTransitionTrigger(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.TRANSITION_WITHOUT_TRIGGER.ordinal();
	}

	@Override
	public String getMessage() {
		return "A transition between states must have a trigger";
	}

}
