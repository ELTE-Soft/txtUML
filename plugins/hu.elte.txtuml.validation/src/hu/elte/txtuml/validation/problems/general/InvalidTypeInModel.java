package hu.elte.txtuml.validation.problems.general;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidTypeInModel extends ValidationErrorBase {

	public InvalidTypeInModel(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_TYPE_IN_MODEL.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.InvalidTypeInModel_message;
	}

}
