package hu.elte.txtuml.validation.problems.datatype;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidDataTypeMethod extends ValidationErrorBase {

	public InvalidDataTypeMethod(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.DATA_TYPE_INVALID_METHOD.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.InvalidDataTypeMethod_message;
	}

}
