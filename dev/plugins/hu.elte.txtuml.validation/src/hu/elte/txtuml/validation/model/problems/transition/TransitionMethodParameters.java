package hu.elte.txtuml.validation.model.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class TransitionMethodParameters extends ValidationErrorBase {

	public TransitionMethodParameters(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.TRANSITION_METHOD_PARAMETERS.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.TransitionMethodParameters_message;
	}

}
