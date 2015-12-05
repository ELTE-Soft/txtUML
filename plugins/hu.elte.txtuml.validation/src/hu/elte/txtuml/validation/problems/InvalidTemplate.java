package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidTemplate extends ValidationErrorBase {
	
	public InvalidTemplate(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return 1;
	}

	@Override
	public String getMessage() {
		return "Templates are not yet supported in txtUML models.";
	}

}
