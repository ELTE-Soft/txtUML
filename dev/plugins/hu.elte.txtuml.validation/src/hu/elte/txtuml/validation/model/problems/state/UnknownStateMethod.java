package hu.elte.txtuml.validation.model.problems.state;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class UnknownStateMethod extends ValidationErrorBase {

	public UnknownStateMethod(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.UNKNOWN_TRANSITION_METHOD.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.UnknownStateMethod_message;
	}

}
