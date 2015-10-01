package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidModelElement extends ValidationErrorBase {
	
	public InvalidModelElement(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return 0;
	}

	@Override
	public String getMessage() {
		return "Invalid element in model. Only model classes, associations and signals are allowed.";
	}

}
