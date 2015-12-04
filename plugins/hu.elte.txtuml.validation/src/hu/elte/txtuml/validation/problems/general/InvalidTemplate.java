package hu.elte.txtuml.validation.problems.general;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidTemplate extends ValidationErrorBase {
	
	public InvalidTemplate(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_TEMPLATE.ordinal();
	}

	@Override
	public String getMessage() {
		return "Templates are not yet supported in txtUML models.";
	}

}
