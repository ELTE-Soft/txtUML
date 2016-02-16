package hu.elte.txtuml.validation.problems.signal;

import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidSignalContent extends ValidationErrorBase {
	
	public InvalidSignalContent(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_SIGNAL_CONTENT.ordinal();
	}

	@Override
	public String getMessage() {
		return Messages.InvalidSignalContent_message;
	}

}
