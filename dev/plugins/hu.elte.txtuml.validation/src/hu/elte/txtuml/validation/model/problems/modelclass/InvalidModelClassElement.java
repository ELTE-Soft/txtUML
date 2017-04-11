package hu.elte.txtuml.validation.model.problems.modelclass;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.SourceInfo;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.model.problems.ValidationErrorCatalog;

public class InvalidModelClassElement extends ValidationErrorBase {

	public static final String message = Messages.InvalidModelClassElement_message;

	public InvalidModelClassElement(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo, node);
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_MODEL_CLASS_ELEMENT.ordinal();
	}

	@Override
	public String getMessage() {
		return message;
	}

}
