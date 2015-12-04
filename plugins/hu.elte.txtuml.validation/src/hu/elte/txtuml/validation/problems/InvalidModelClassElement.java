package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidModelClassElement extends ValidationErrorBase {
	
	public static final String message = "Invalid element in model class. Only states, transitions, operations and attributes are allowed.";
	
	public InvalidModelClassElement(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return 4;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
