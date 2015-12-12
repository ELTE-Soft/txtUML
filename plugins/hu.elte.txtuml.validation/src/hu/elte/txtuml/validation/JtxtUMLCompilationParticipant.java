package hu.elte.txtuml.validation;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.diagnostics.PluginLogWrapper;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.validation.visitors.ModelVisitor;

/**
 * Compilation participant for JtxtUML validation.
 * 
 */
public class JtxtUMLCompilationParticipant extends org.eclipse.jdt.core.compiler.CompilationParticipant {
	private static final String TXTUML_NATURE_ID = "hu.elte.txtuml.project.txtumlprojectNature";
	public static final String JTXTUML_MARKER_TYPE = "hu.elte.txtuml.validation.jtxtumlmarker";

	@Override
	public boolean isActive(IJavaProject project) {
		try {
			return project.getProject().getNature(TXTUML_NATURE_ID) != null;
		} catch (CoreException e) {
			PluginLogWrapper.logError("Error while checking txtuml project", e);
			return false;
		}
	}

	@Override
	public int aboutToBuild(IJavaProject project) {
		return READY_FOR_BUILD;
	}

	@Override
	public void reconcile(ReconcileContext context) {
		try {
			CompilationUnit unit = context.getAST8();
			ProblemCollector collector = new ProblemCollector(context);
			if (ElementTypeTeller.isModelElement(unit)) {
				unit.accept(new ModelVisitor(collector));
			}
			collector.refreshProblems();
		} catch (Exception e) {
			PluginLogWrapper.logError("Error while checking for problems", e);
		}
	}

	@Override
	public void buildStarting(BuildContext[] files, boolean isBatch) {
		for (BuildContext file : files) {
			validateFile(file);
		}
	}

	private void validateFile(BuildContext file) {
		java.io.File systemFile = null;
		IPath location = file.getFile().getLocation();
		if (location != null) {
			systemFile = location.toFile();
		}
		if (systemFile == null) {
			return;
		}

		IProject project = file.getFile().getProject();
		IJavaProject javaProject = JavaCore.create(project);
		CompilationUnit unit = null;
		try {
			unit = SharedUtils.parseJavaSource(systemFile, javaProject);
		} catch (IOException | JavaModelException e) {
			// Validation is not possible, return.
			return;
		}
		if (unit == null || !ElementTypeTeller.isModelElement(unit)) {
			// Validation is not possible, return.
			return;
		}

		try {
			ProblemCollector collector = new ProblemCollector(unit, file.getFile());
			unit.accept(new ModelVisitor(collector));
			collector.refreshProblems();
		} catch (JavaModelException e) {
			PluginLogWrapper.logError("Error while creating problem collector", e);
		}
	}

}
