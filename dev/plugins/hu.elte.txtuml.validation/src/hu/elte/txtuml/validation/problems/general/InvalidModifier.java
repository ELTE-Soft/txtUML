package hu.elte.txtuml.validation.problems.general;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidModifier extends ValidationErrorBase {

	public InvalidModifier(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_MODIFIER.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.InvalidModifier_message;
	}

}
