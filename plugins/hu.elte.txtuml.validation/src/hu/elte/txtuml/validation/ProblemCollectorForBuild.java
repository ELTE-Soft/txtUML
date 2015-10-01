package hu.elte.txtuml.validation;

import hu.elte.txtuml.validation.problems.ValidationErrorBase;

import java.util.ArrayList;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Collect problems in a container for later use.
 * To be used for the build-time validation.
 */
public class ProblemCollectorForBuild extends ProblemCollector {
	
	private ArrayList<ValidationErrorBase> problems;
	private SourceInfo sourceInfo;

	public ProblemCollectorForBuild(String name, CompilationUnit compUnit) {
		this.problems = new ArrayList<ValidationErrorBase>();
		this.sourceInfo = new SourceInfoForBuild(name, compUnit);
	}
	
	@Override
	public void setProblemStatus(boolean add, ValidationErrorBase problem) {
		if(add) {
			problems.add(problem);
		}
	}
	
	public CategorizedProblem[] getProblems() {
		CategorizedProblem[] array = new CategorizedProblem[problems.size()];
		return problems.toArray(array);
	}

	@Override
	public SourceInfo getSourceInfo() {
		return this.sourceInfo;
	}
}
