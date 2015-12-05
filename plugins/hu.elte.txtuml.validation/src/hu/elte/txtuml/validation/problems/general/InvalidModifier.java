package hu.elte.txtuml.validation.problems.general;

import hu.elte.txtuml.validation.SourceInfo;
import hu.elte.txtuml.validation.problems.ValidationErrorBase;
import hu.elte.txtuml.validation.problems.ValidationErrorCatalog;

import org.eclipse.jdt.core.dom.ASTNode;

public class InvalidModifier extends ValidationErrorBase {
	
	public InvalidModifier(SourceInfo sourceInfo, ASTNode node) {
		super(sourceInfo,node);
	}
	
	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_MODIFIER.ordinal();
	}

	@Override
	public String getMessage() {
		return "Invalid modifier. Only private and public are allowed on all elements. Static is allowed only on signals.";
	}

}
