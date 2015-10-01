package hu.elte.txtuml.export.uml2;

import hu.elte.txtuml.export.uml2.transform.importers.ModelImporter;
import hu.elte.txtuml.export.uml2.transform.visitors.ModelObtainer;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a
 * txtUML model.
 * 
 * @author Adam Ancsin
 *
 */
public class UML2 {
	
	public static void exportModel(String sourceProject, String className, String outputDirectory)
			throws Exception {
		IProject project;
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(sourceProject);
		if(project == null || !project.exists()) {
			throw new Exception("Cannot find project '" + sourceProject + "'");			
		}
		
		IJavaProject javaProject = JavaCore.create(project);
		IType type = javaProject.findType(className, (IProgressMonitor)null);
		if(type == null) {
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
	private static TypeDeclaration obtainModelFromCompilationUnit(CompilationUnit compilationUnit) {
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
	 * @throws Exception
	 * 
	 * @author Adam Ancsin
	 */
	public static void exportModel(String txtUMLModelName, File sourceFile, IJavaProject project, String outputDirectory)
			throws Exception {

		CompilationUnit compilationUnit = SharedUtils.parseJavaSource(sourceFile, project);

		ModelImporter modelImporter = new ModelImporter(obtainModelFromCompilationUnit(compilationUnit));
		Model model = modelImporter.importModel(txtUMLModelName, outputDirectory);

		ResourceSet resourceSet = modelImporter.getResourceSet();

		Resource modelResource = modelImporter.getModelResource();
		modelResource.save(null); // no save options needed

		// create resource for profile and save profile
		Profile profile = modelImporter.getProfile();
		Resource profileResource = resourceSet.createResource(URI
				.createURI(outputDirectory)
				.appendSegment(model.getQualifiedName())
				.appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION));
		profileResource.getContents().add(profile);
		profileResource.save(null); // no save options needed

		// delete surplus profile
		if (!outputDirectory.isEmpty()) {
			Path path = FileSystems.getDefault().getPath(
					model.getQualifiedName() + "."
							+ UMLResource.PROFILE_FILE_EXTENSION);
			Files.delete(path);
		}

	}
}
