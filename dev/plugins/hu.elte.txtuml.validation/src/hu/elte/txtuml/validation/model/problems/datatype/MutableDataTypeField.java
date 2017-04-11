package hu.elte.txtuml.validation.model.problems.datatype;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class MutableDataTypeField extends ValidationErrorBase {

	public MutableDataTypeField(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.DATA_TYPE_FIELD_NOT_FINAL.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.MutableDataTypeField_message;
	}

}
