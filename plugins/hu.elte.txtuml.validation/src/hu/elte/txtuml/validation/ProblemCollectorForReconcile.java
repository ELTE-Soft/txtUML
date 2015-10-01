package hu.elte.txtuml.validation;

import hu.elte.txtuml.validation.problems.ValidationErrorBase;

import java.util.Arrays;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.ReconcileContext;

/**
 * Collects problems into a ReconcileContext.
 * To be used for reconcile-time validation.
 */
public class ProblemCollectorForReconcile extends ProblemCollector {

	private ReconcileContext context;
	private SourceInfo sourceInfo;
	private IMarker[] markers;
	
	public ProblemCollectorForReconcile(ReconcileContext context, IMarker[] markers) {
		this.context = context;
		this.sourceInfo = new SourceInfoForReconcile(context.getWorkingCopy());
		this.markers = markers;
	}
	
	@Override
	public void setProblemStatus(boolean add, ValidationErrorBase problem) {
		if(add) {
			CategorizedProblem[] existingProblems = context.getProblems(problem.getMarkerType());
	        CategorizedProblem[] newProblems;
	        if (existingProblems == null)
	            newProblems = new CategorizedProblem[]{problem};
	        else {
	            newProblems = Arrays.copyOf(existingProblems, existingProblems.length+1);
	            newProblems[existingProblems.length] = problem;
	        }
	        context.putProblems(problem.getMarkerType(), newProblems);
		} else {
			for(IMarker marker : markers) {
				try {
					int start = (Integer)marker.getAttribute(IMarker.CHAR_START);
					String message = (String)marker.getAttribute(IMarker.MESSAGE);
					if(start == problem.getSourceStart() && message.equals(problem.getMessage())) {
						marker.delete();
					}
				} catch (CoreException e) {
				}
			}
		}
	}

	@Override
	public SourceInfo getSourceInfo() {
		return sourceInfo;
	}

}
