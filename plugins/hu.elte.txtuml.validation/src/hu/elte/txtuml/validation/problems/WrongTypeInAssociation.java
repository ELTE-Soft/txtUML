package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class WrongTypeInAssociation extends ValidationErrorBase {

	public WrongTypeInAssociation(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.WRONG_TYPE_IN_ASSOCIATION.ordinal();
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
