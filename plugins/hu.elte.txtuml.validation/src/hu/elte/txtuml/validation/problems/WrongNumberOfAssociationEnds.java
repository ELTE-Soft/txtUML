package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class WrongNumberOfAssociationEnds extends ValidationErrorBase {

	public WrongNumberOfAssociationEnds(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.WRONG_NUMBER_OF_ASSOCIATION_ENDS.ordinal();
	}

	@Override
	public String getMessage() {
		return "An association must have 2 ends.";
	}

}
