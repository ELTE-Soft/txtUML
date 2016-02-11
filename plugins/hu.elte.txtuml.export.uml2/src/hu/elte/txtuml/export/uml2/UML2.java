package hu.elte.txtuml.export.uml2;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.RuntimeExportException;
import hu.elte.txtuml.export.uml2.transform.exporters.ModelExporter;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a
 * txtUML model.
 * 
 * @author Adam Ancsin
 *
 */
public class UML2 {

	public static void exportModel(String sourceProject, String modelName, String outputDirectory) throws Exception {
		IProject project;
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(sourceProject);
		if (project == null || !project.exists()) {
			throw new Exception("Cannot find project '" + sourceProject + "'");
		}

		IJavaProject javaProject = JavaCore.create(project);
		String modelPackageName = modelName.substring(0, modelName.lastIndexOf('.'));

		List<ICompilationUnit> compUnits = new LinkedList<>();

		for (IPackageFragment fragment : javaProject.getPackageFragments()) {
			if (fragment.getElementName().equals(modelPackageName)) {
				for (ICompilationUnit compUnit : fragment.getCompilationUnits()) {
					compUnits.add(compUnit);
				}
			}
		}

		exportModelElements(modelName, compUnits, javaProject, outputDirectory);
	}

	/**
	 * Exports UML2 model generated from a txtUML model.
	 * 
	 * @param txtUMLModelName
	 *            The name of the class representing the txtUML model.
	 * 
	 * @param compUnits
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
	public static void exportModelElements(String txtUMLModelName, List<ICompilationUnit> compUnits,
			IJavaProject project, String outputDirectory) throws JavaModelException, IOException, ExportException {

		List<CompilationUnit> parsedCompUnits = new LinkedList<>();

		for (ICompilationUnit compUnit : compUnits) {
			IResource resource = compUnit.getResource();
			File file = new File(resource.getLocationURI());
			parsedCompUnits.add(SharedUtils.parseJavaSource(file, project));
		}

		ModelExporter modelExporter = new ModelExporter(parsedCompUnits, txtUMLModelName, outputDirectory);
		try {
			modelExporter.exportModel();

			Resource modelResource = modelExporter.getModelResource();
			modelResource.save(null); // no save options needed
		} catch (RuntimeExportException e) {
			throw e.getCause();
		}

	}
}
