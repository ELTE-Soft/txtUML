package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidTypeWithClassAllowed extends ValidationErrorBase {

	public static final String message = "Invalid type. Only boolean, double, int, String and model class types are allowed.";

	public InvalidTypeWithClassAllowed(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return 3;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
