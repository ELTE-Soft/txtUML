package hu.elte.txtuml.validation;

import hu.elte.txtuml.validation.problems.ValidationErrorBase;

public abstract class ProblemCollector {
	/**
	 * Adds a new problem or deletes a marker from earlier build.
	 * 
	 * @param add		If true, the problem is added; if false, the problem is deleted.
	 * @param problem	The problem to add/delete.
	 */
	public abstract void setProblemStatus(boolean add, ValidationErrorBase problem);
	
	public abstract SourceInfo getSourceInfo();
}
