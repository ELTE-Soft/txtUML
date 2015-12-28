package hu.elte.txtuml.validation.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class TransitionToOutside extends ValidationErrorBase {

	public TransitionToOutside(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.TRANSITION_TO_OUTSIDE.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.TransitionToOutside_message;
	}

}
