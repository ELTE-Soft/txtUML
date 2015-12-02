package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class StateMethodNonVoidReturn extends ValidationErrorBase {

	public StateMethodNonVoidReturn(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.STATE_METHOD_NONVOID_RETURN.ordinal();
	}

	@Override
	public String getMessage() {
		return "Methods in a state must have a void return type";
	}

}
