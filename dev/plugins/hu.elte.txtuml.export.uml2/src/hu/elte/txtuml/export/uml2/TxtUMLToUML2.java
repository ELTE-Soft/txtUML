package hu.elte.txtuml.export.uml2;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.resource.UMLResource;

import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.RuntimeExportException;
import hu.elte.txtuml.export.uml2.transform.exporters.ModelExporter;
import hu.elte.txtuml.export.uml2.utils.ResourceSetFactory;
import hu.elte.txtuml.utils.Sneaky;
import hu.elte.txtuml.utils.eclipse.NotFoundException;
import hu.elte.txtuml.utils.eclipse.PackageUtils;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;
import hu.elte.txtuml.utils.jdt.ModelUtils;
import hu.elte.txtuml.utils.jdt.SharedUtils;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a
 * txtUML model.
 */
public class TxtUMLToUML2 {

	public enum ExportMode {
		ExportDefinitions, ExportActionCode
	}

	/**
	 * Exports the txtUML model to a org.eclipse.uml2.uml.Model representation
	 * 
	 * @param sourceProject
	 *            name of the source project, where the txtUML model can be find
	 * @param packageName
	 *            fully qualified name of the txtUML model
	 * @param outputDirectory
	 *            where the result model should be saved
	 */
	public static Model exportModel(String sourceProject, String packageName, String outputDirectory,
			ExportMode exportMode) throws JavaModelException, NotFoundException, IOException, ExportException {
		return exportModel(sourceProject, packageName, URI.createPlatformResourceURI(outputDirectory, false),
				exportMode);
	}

	/**
	 * Exports the txtUML model to a org.eclipse.uml2.uml.Model representation
	 * 
	 * @param sourceProject
	 *            name of the source project, where the txtUML model can be find
	 * @param packageName
	 *            fully qualified name of the txtUML model
	 * @param outputDirectory
	 *            where the result model should be saved
	 */
	public static Model exportModel(String sourceProject, String packageName, URI outputDirectory,
			ExportMode exportMode) throws NotFoundException, JavaModelException, IOException, ExportException {

		IJavaProject javaProject = ProjectUtils.findJavaProject(sourceProject);

		IPackageFragment[] packageFragments = PackageUtils.findPackageFragments(javaProject, packageName);

		if (packageFragments.length == 0) {
			throw new NotFoundException("Cannot find package '" + packageName + "'");
		}

		Model model = new hu.elte.txtuml.export.uml2.restructured.structural.ModelExporter().export(packageFragments[0],
				packageFragments[0]);
		URI uri = URI.createFileURI(javaProject.getProject().getLocation().toOSString()).appendSegment("gen")
				.appendSegment(packageName).appendFileExtension(UMLResource.FILE_EXTENSION);
		ResourceSet resourceSet = new ResourceSetFactory().createAndInitResourceSet();
		Resource modelResource = resourceSet.createResource(uri);
		modelResource.getContents().add(model);
		new File(uri.toFileString()).getParentFile().mkdirs();
		modelResource.save(null);
		return model;
	}

	/**
	 * Obtains the txtUML model from the specified compilation unit.
	 * 
	 * @param compilationUnit
	 *            the specified compilation unit
	 * @return the type declaration of the txtUML model
	 */
	private static Optional<String> obtainModelFromCompilationUnits(String packageName,
			CompilationUnit[] compilationUnits) {

		Optional<String> ret;

		for (CompilationUnit cu : compilationUnits) {
			ret = ModelUtils.findModelNameInTopPackage(cu);
			if (ret.isPresent()) {
				return ret;
			}
		}

		return Optional.empty();
	}

	/**
	 * Exports UML2 model generated from a txtUML model.
	 * 
	 * @param txtUMLModelName
	 *            the name of the class representing the txtUML model
	 * @param sourceFile
	 *            the Java source file containing the txtUML model
	 * @param javaProject
	 *            the Java project contatining the txtUML model
	 * @param outputDirectory
	 *            the name of the output directory. (relative to the path of the
	 *            project containing the txtUML model)
	 */
	public static Model exportModel(String packageName, IPackageFragment[] packageFragments, IJavaProject javaProject,
			URI outputDirectory, ExportMode exportMode) throws JavaModelException, IOException, ExportException {

		Stream<ICompilationUnit> packageInfo = Stream.of(packageFragments)
				.filter(pf -> pf.getElementName().equals(packageName))
				.map(pf -> pf.getCompilationUnit(PackageUtils.PACKAGE_INFO)).filter(ICompilationUnit::exists);

		Optional<String> JtxtUMLModelName = obtainModelFromCompilationUnits(packageName,
				SharedUtils.parseICompilationUnitStream(packageInfo, javaProject));

		if (!JtxtUMLModelName.isPresent()) {
			throw new ExportException("Package '" + packageName + "' is not a JtxtUML model.");
		}

		// Sneaky.<JavaModelException> Throw();
		Stream<ICompilationUnit> all = Stream.of(packageFragments)
				.flatMap(Sneaky.unchecked(pf -> Stream.of(pf.getCompilationUnits())));

		ModelExporter modelExporter = new ModelExporter(SharedUtils.parseICompilationUnitStream(all, javaProject),
				JtxtUMLModelName.get(), packageName, outputDirectory, exportMode);
		try {
			modelExporter.exportModel();

			Resource modelResource = modelExporter.getModelResource();
			modelResource.save(null); // no save options needed

			return modelExporter.getExportedModel();
		} catch (RuntimeExportException e) {
			throw e.getCause();
		}

	}

	public static Model loadExportedModel(String uri) throws WrappedException {
		return loadExportedModel(URI.createPlatformResourceURI(uri, false));

	}

	public static Model loadExportedModel(URI uri) throws WrappedException {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(uri, true);
		Model model = null;
		if (resource.getContents().size() != 0)
			model = (Model) resource.getContents().get(0);
		return model;
	}

}