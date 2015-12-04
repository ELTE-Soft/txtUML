package hu.elte.txtuml.validation.problems.modelclass;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidTypeWithClassAllowed extends ValidationErrorBase {

	public static final String message = "Invalid type. Only boolean, double, int, String and model class types are allowed.";

	public InvalidTypeWithClassAllowed(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
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
