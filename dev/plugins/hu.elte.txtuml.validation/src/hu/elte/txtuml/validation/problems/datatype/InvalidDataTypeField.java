package hu.elte.txtuml.validation.problems.datatype;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidDataTypeField extends ValidationErrorBase {

	public InvalidDataTypeField(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.DATA_TYPE_FIELD_INVALID_TYPE.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.InvalidDataTypeField_message;
	}

}
