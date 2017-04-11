package hu.elte.txtuml.validation.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.validation.model.problems.ValidationErrorBase;

public class ProblemCollector {

	private ArrayList<ValidationErrorBase> problems = new ArrayList<>();
	private SourceInfo sourceInfo;
	private IResource resource;

	public ProblemCollector(CompilationUnit unit, IFile file) throws JavaModelException {
		this.problems = new ArrayList<ValidationErrorBase>();
		this.sourceInfo = new SourceInfo(unit);
		resource = file;
	}

	public ProblemCollector(ReconcileContext context) throws JavaModelException {
		resource = context.getWorkingCopy().getResource();
		this.sourceInfo = new SourceInfo(context.getAST8());
	}

	/**
	 * Adds a new problem.
	 */
	public void report(ValidationErrorBase problem) {
		problems.add(problem);
	}

	public SourceInfo getSourceInfo() {
		return sourceInfo;
	}

	public void refreshProblems() {
		if (resource == null) {
			// collector is not active
			return;
		}
		try {
			resource.deleteMarkers(JtxtUMLCompilationParticipant.JTXTUML_MARKER_TYPE, true, IResource.DEPTH_ZERO);
			for (ValidationErrorBase problem : problems) {
				IMarker marker = resource.createMarker(problem.getMarkerType());
				marker.setAttribute(IMarker.CHAR_START, problem.getSourceStart());
				marker.setAttribute(IMarker.CHAR_END, problem.getSourceEnd());
				marker.setAttribute(IMarker.SEVERITY,
						problem.isWarning() ? IMarker.SEVERITY_WARNING : IMarker.SEVERITY_ERROR);
				marker.setAttribute(IMarker.MESSAGE, problem.getMessage());
			}
			problems.clear();
		} catch (CoreException e) {
			Logger.sys.error("Error while refreshing problem markers", e); //$NON-NLS-1$
		}
	}

}
