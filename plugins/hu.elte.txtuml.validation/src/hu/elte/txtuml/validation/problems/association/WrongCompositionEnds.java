package hu.elte.txtuml.validation.problems.association;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

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
		return Messages.WrongCompositionEnds_message;
	}

}
