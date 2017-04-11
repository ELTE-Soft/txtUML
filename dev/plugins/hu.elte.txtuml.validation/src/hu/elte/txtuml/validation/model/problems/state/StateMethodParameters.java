package hu.elte.txtuml.validation.model.problems.state;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class StateMethodParameters extends ValidationErrorBase {

	public StateMethodParameters(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.STATE_METHOD_PARAMETERS.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.StateMethodParameters_message;
	}

}
