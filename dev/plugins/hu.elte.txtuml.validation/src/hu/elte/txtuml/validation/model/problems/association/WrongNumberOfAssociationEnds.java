package hu.elte.txtuml.validation.model.problems.association;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

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
		return Messages.WrongNumberOfAssociationEnds_message;
	}

}
