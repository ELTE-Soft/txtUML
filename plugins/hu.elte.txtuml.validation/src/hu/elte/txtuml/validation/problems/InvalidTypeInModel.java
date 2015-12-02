package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

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
		return "Invalid type declaration in model. Only model classes, signals and associations can be part of the model";
	}

}
