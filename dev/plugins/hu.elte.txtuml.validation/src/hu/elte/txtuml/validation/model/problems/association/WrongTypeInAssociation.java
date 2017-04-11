package hu.elte.txtuml.validation.model.problems.association;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

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
		return Messages.WrongTypeInAssociation_message;
	}

}
