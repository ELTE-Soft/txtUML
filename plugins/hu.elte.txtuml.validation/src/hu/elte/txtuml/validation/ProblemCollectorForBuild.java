package hu.elte.txtuml.validation;

import hu.elte.txtuml.validation.problems.ValidationErrorBase;

import java.util.ArrayList;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Collect problems in a container for later use. To be used for the build-time
 * validation. It is assumed that the build gets all the problems and displays
 * them.
 */
public class ProblemCollectorForBuild extends ProblemCollector {

	private ArrayList<ValidationErrorBase> problems;
	private SourceInfo sourceInfo;

	public ProblemCollectorForBuild(CompilationUnit compUnit) {
		this.problems = new ArrayList<ValidationErrorBase>();
		this.sourceInfo = new ASTSourceInfo(compUnit);
	}

	@Override
	public void setProblemStatus(boolean add, ValidationErrorBase problem) {
		if (add) {
			problems.add(problem);
		} else {
			problems.remove(problem);
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
