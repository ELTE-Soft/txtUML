package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class StateMethodParameters extends ValidationErrorBase {

	public StateMethodParameters(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.STATE_METHOD_PARAMETERS.ordinal();
	}

	@Override
	public String getMessage() {
		return "A state entry or exit action cannot have parameters";
	}

}
