package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidTypeWithClassNotAllowed extends ValidationErrorBase {

	public static final String message = "Invalid type. Only boolean, double, int and String are allowed.";
	
	public InvalidTypeWithClassNotAllowed(SourceInfo sourceInfo, ASTNode node) {
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
