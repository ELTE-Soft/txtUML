package hu.elte.txtuml.validation.problems.modelclass;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidTypeWithClassAllowed extends ValidationErrorBase {

	public static final String message = Messages.InvalidTypeWithClassAllowed_message;

	public InvalidTypeWithClassAllowed(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_TYPE_WITH_CLASS_ALLOWED.ordinal();
	}

	@Override
	public String getMessage() {
		return message;
	}

}
