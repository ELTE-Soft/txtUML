package hu.elte.txtuml.validation.problems;

import hu.elte.txtuml.validation.SourceInfo;

import org.eclipse.jdt.core.dom.ASTNode;

public class WrongCompositionEnds extends ValidationErrorBase {

	public WrongCompositionEnds(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.WRONG_COMPOSITION_ENDS.ordinal();
	}

	@Override
	public String getMessage() {
		return "A composition must have exactly one container end and exactly one part end.";
	}

}
