package hu.elte.txtuml.export.uml2;

import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.RuntimeExportException;
import hu.elte.txtuml.export.uml2.transform.exporters.ModelExporter;
import hu.elte.txtuml.export.uml2.transform.visitors.ModelObtainer;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a
 * txtUML model.
 * 
 * @author Adam Ancsin
 *
 */
public class UML2 {

	public static void exportModel(String sourceProject, String className,
			String outputDirectory) throws Exception {
		IProject project;
		project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(sourceProject);
		if (project == null || !project.exists()) {
			throw new Exception("Cannot find project '" + sourceProject + "'");
		}

		IJavaProject javaProject = JavaCore.create(project);
		IType type = javaProject.findType(className, (IProgressMonitor) null);
		if (type == null) {
			throw new Exception("Cannot find class '" + className + "'");
		}

		IResource resource = type.getResource();
		File file = new File(resource.getLocationURI());
		exportModel(className, file, javaProject, outputDirectory);
	}

	/**
	 * Obtains the txtUML model from the specified compilation unit.
	 * 
	 * @param compilationUnit
	 *            The specified compilation unit.
	 * @return The type declaration of the txtUML model.
	 *
	 * @author Ádám Ancsin
	 */
	private static TypeDeclaration obtainModelFromCompilationUnit(
			CompilationUnit compilationUnit) {
		return new ModelObtainer(compilationUnit).getModel();
	}

	/**
	 * Exports UML2 model generated from a txtUML model.
	 * 
	 * @param txtUMLModelName
	 *            The name of the class representing the txtUML model.
	 * 
	 * @param sourceFile
	 *            The Java source file containing the txtUML model.
	 * 
	 * @param project
	 *            The Java project contatining the txtUML model.
	 * 
	 * @param outputDirectory
	 *            The name of the output directory. (relative to the path of the
	 *            project containing the txtUML model)
	 * @throws IOException
	 * @throws JavaModelException
	 * @throws ExportException
	 * 
	 * @author Adam Ancsin
	 */
	public static void exportModel(String txtUMLModelName, File sourceFile,
			IJavaProject project, String outputDirectory)
			throws JavaModelException, IOException, ExportException {

		CompilationUnit compilationUnit = SharedUtils.parseJavaSource(
				sourceFile, project);

		ModelExporter modelExporter = new ModelExporter(
				obtainModelFromCompilationUnit(compilationUnit),
				txtUMLModelName, outputDirectory);
		try {
			modelExporter.exportModel();

			Resource modelResource = modelExporter.getModelResource();
			modelResource.save(null); // no save options needed
		} catch (RuntimeExportException e) {
			throw e.getCause();
		}

	}
}
