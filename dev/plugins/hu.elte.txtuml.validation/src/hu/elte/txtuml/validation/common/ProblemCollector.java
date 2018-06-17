package hu.elte.txtuml.validation.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.utils.Logger;

public class ProblemCollector {

	private final List<IValidationProblem> problems = new ArrayList<>();
	private final String markerType;
	private final SourceInfo sourceInfo;
	private final IResource resource;

	public ProblemCollector(String markerType, CompilationUnit unit, IResource resource) throws JavaModelException {
		this.markerType = markerType;
		this.sourceInfo = new SourceInfo(unit);
		this.resource = resource;
	}

	public ProblemCollector(String markerType, ReconcileContext context) throws JavaModelException {
		this(markerType, context.getAST8(), context.getWorkingCopy().getResource());
	}

	/**
	 * Adds a new problem.
	 */
	public void report(IValidationProblem problem) {
		problems.add(problem);
	}

	public SourceInfo getSourceInfo() {
		return sourceInfo;
	}

	public void refreshProblems() {
		if (resource == null) {
			// Collector is not active.
			return;
		}
		try {
			resource.deleteMarkers(markerType, true, IResource.DEPTH_ZERO);
			for (IValidationProblem problem : problems) {
				IMarker marker = resource.createMarker(problem.getMarkerType());
				marker.setAttribute(IMarker.CHAR_START, problem.getSourceStart());
				marker.setAttribute(IMarker.CHAR_END, problem.getSourceEnd());
				int severity;
				if (problem.isError()) {
					severity = IMarker.SEVERITY_ERROR;
				} else if (problem.isWarning()) {
					severity = IMarker.SEVERITY_WARNING;
				} else {
					severity = IMarker.SEVERITY_INFO;
				}
				marker.setAttribute(IMarker.SEVERITY, severity);
				marker.setAttribute(IMarker.MESSAGE, problem.getMessage());
			}
			problems.clear();
		} catch (CoreException e) {
			Logger.sys.error("Error while refreshing problem markers", e); //$NON-NLS-1$
		}
	}

}
