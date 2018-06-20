package hu.elte.txtuml.validation.common;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Interface to create different validation errors.
 */
public interface IValidationErrorType {

	IValidationProblem create(SourceInfo sourceInfo, ASTNode node);

}
