package hu.elte.txtuml.validation.problems.transition;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class TransitionMethodNonVoidReturn extends ValidationErrorBase {

	public TransitionMethodNonVoidReturn(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.TRANSITION_METHOD_NONVOID_RETURN.ordinal();
	}

	@Override
	public String getMessage() {
		return "The effect of a transition must have a void return type";
	}

}
