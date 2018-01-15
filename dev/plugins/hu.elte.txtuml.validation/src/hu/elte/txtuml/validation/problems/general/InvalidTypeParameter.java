package hu.elte.txtuml.validation.problems.general;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidTypeParameter extends ValidationErrorBase {

	public InvalidTypeParameter(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_TYPE_PARAMETER.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.InvalidTypeParameter_message;
	}

}
