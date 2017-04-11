package hu.elte.txtuml.validation.model.problems.modelclass;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class InvalidAttributeType extends ValidationErrorBase {

	public static final String message = Messages.InvalidAttributeType_message;

	public InvalidAttributeType(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_ATTRIBUTE_TYPE.ordinal();
	}

	@Override
	public String getMessage() {
		return message;
	}

}
