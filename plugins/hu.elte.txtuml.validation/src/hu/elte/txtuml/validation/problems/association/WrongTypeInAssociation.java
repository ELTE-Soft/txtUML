package hu.elte.txtuml.validation.problems.association;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

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
		return "Member classes of association must be association ends.";
	}

}
