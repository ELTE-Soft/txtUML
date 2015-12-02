package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class UnknownStateMethod extends ValidationErrorBase {

	public UnknownStateMethod(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.UNKNOWN_STATE_METHOD.ordinal();
	}

	@Override
	public String getMessage() {
		return "Only entry and exit methods can be in a state";
	}

}
