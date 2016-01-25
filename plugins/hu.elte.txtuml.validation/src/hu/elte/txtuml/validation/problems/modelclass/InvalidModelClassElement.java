package hu.elte.txtuml.validation.problems.modelclass;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

public class InvalidModelClassElement extends ValidationErrorBase {
	
	public static final String message = Messages.InvalidModelClassElement_message;
	
	public InvalidModelClassElement(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
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
