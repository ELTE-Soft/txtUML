package hu.elte.txtuml.validation.common;

import org.eclipse.jdt.core.compiler.IProblem;

/**
 * Interface for all kind of validation problems.
 */
public interface IValidationProblem extends IProblem {

	String getMarkerType();

}
