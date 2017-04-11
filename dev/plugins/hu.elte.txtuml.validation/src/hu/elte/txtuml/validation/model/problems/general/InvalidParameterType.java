package hu.elte.txtuml.validation.model.problems.general;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class InvalidParameterType extends ValidationErrorBase {

	public static final String message = Messages.InvalidParameterType_message;

	public InvalidParameterType(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_PARAMETER_TYPE.ordinal();
	}

	@Override
	public String getMessage() {
		return message;
	}

}
