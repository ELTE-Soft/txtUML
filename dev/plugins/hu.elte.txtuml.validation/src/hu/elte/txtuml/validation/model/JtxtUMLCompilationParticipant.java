package hu.elte.txtuml.validation.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.utils.jdt.SharedUtils;
import hu.elte.txtuml.validation.model.visitors.ModelVisitor;

/**
 * Compilation participant for JtxtUML validation. Performs validation on
 * reconcile events (not typing for a few seconds) and build events
 */
public class JtxtUMLCompilationParticipant extends org.eclipse.jdt.core.compiler.CompilationParticipant {

	private static final String TXTUML_NATURE_ID = "hu.elte.txtuml.project.txtumlprojectNature"; //$NON-NLS-1$
	public static final String JTXTUML_MARKER_TYPE = "hu.elte.txtuml.validation.jtxtumlmarker"; //$NON-NLS-1$

	private Map<IJavaProject, Set<BuildContext>> javaProjectToFileSet = new HashMap<>();

	@Override
	public boolean isActive(IJavaProject project) {
		try {
			return project.getProject().getNature(TXTUML_NATURE_ID) != null;
		} catch (CoreException e) {
			Logger.sys.error("Error while checking txtuml project", e); //$NON-NLS-1$
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
			Logger.sys.error("Error while checking for problems", e); //$NON-NLS-1$
		}
	}

	@Override
	public void buildStarting(BuildContext[] files, boolean isBatch) {
		for (BuildContext file : files) {
			IProject project = file.getFile().getProject();
			IJavaProject javaProject = JavaCore.create(project);

			Set<BuildContext> projectFileSet = javaProjectToFileSet.get(javaProject);
			if (projectFileSet == null) {
				projectFileSet = new HashSet<>();
				javaProjectToFileSet.put(javaProject, projectFileSet);
			}

			projectFileSet.add(file);
		}
	}

	@Override
	public void buildFinished(IJavaProject javaProject) {
		Set<BuildContext> projectFileSet = javaProjectToFileSet.get(javaProject);
		if (projectFileSet == null) { // buildFinished might be called without
										// buildStarting
			return;
		}

		for (BuildContext file : projectFileSet) {
			validateFile(file, javaProject);
		}

		javaProjectToFileSet.remove(javaProject);
	}

	private void validateFile(BuildContext file, IJavaProject javaProject) {
		File systemFile = null;
		IPath location = file.getFile().getLocation();
		if (location != null) {
			systemFile = location.toFile();
		}
		if (systemFile == null) {
			return;
		}

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
			Logger.sys.error("Error while creating problem collector", e); //$NON-NLS-1$
		}
	}

}
