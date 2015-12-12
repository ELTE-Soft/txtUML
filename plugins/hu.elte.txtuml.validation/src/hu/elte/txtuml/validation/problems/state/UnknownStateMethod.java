package hu.elte.txtuml.validation.problems.state;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

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
