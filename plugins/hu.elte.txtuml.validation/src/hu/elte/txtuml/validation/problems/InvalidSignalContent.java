package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidSignalContent extends ValidationErrorBase {
	
	public InvalidSignalContent(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return 2;
	}

	@Override
	public String getMessage() {
		return "Invalid element in signal. Only attributes and constructors are allowed.";
	}

}
