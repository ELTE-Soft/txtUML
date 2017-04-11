package hu.elte.txtuml.validation.model.problems.association;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

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
		return Messages.WrongCompositionEnds_message;
	}

}
