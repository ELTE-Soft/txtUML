package hu.elte.txtuml.export.uml2;

import hu.elte.txtuml.eclipseutils.NotFoundException;
import hu.elte.txtuml.eclipseutils.PackageUtils;
import hu.elte.txtuml.eclipseutils.ProjectUtils;
import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.RuntimeExportException;
import hu.elte.txtuml.export.uml2.transform.exporters.ModelExporter;
import hu.elte.txtuml.export.uml2.transform.visitors.ModelObtainer;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.utils.Sneaky;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * This class is responsible for exporting Eclipse UML2 model generated from a
 * txtUML model.
 * 
 * @author Adam Ancsin
 *
 */
public class UML2 {

	public static void exportModel(String sourceProject, String packageName,
			String outputDirectory) throws NotFoundException,
			JavaModelException, IOException, ExportException {

		IJavaProject javaProject = ProjectUtils.findJavaProject(sourceProject);

		IPackageFragment[] packageFragments = PackageUtils
				.findPackageFragments(javaProject, packageName);

		if (packageFragments.length == 0) {
			throw new NotFoundException("Cannot find package '" + packageName
					+ "'");
		}

		exportModel(packageName, packageFragments, javaProject, outputDirectory);
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
	private static Optional<String> obtainModelFromCompilationUnit(
			String packageName, CompilationUnit[] compilationUnits) {
		return new ModelObtainer(packageName, compilationUnits).getModelName();
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
	 * @param javaProject
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
	public static void exportModel(String packageName,
			IPackageFragment[] packageFragments, IJavaProject javaProject,
			String outputDirectory) throws JavaModelException, IOException,
			ExportException {

		Stream<ICompilationUnit> packageInfo = Stream.of(packageFragments)
				.filter(pf -> pf.getElementName().equals(packageName))
				.map(pf -> pf.getCompilationUnit("package-info.java"))
				.filter(ICompilationUnit::exists);

		Optional<String> JtxtUMLModelName = obtainModelFromCompilationUnit(
				packageName, toCompilationUnits(packageInfo, javaProject));

		if (!JtxtUMLModelName.isPresent()) {
			throw new ExportException("Package '" + packageName
					+ "' is not a JtxtUML model.");
		}

		// Sneaky.<JavaModelException> Throw();
		Stream<ICompilationUnit> all = Stream.of(packageFragments).flatMap(
				Sneaky.unchecked(pf -> Stream.of(pf.getCompilationUnits())));

		ModelExporter modelExporter = new ModelExporter(toCompilationUnits(all,
				javaProject), JtxtUMLModelName.get(), packageName, outputDirectory);
		try {
			modelExporter.exportModel();

			Resource modelResource = modelExporter.getModelResource();
			modelResource.save(null); // no save options needed
		} catch (RuntimeExportException e) {
			throw e.getCause();
		}

	}

	private static CompilationUnit[] toCompilationUnits(
			Stream<ICompilationUnit> stream, IJavaProject javaProject)
			throws IOException, JavaModelException {

		// Sneaky.<JavaModelException> Throw();
		// Sneaky.<IOException> Throw();
		return stream
				.map(ICompilationUnit::getResource)
				.map(IResource::getLocationURI)
				.map(File::new)
				.map(Sneaky.unchecked(f -> SharedUtils.parseJavaSource(f,
						javaProject))).filter(Objects::nonNull)
				.toArray(CompilationUnit[]::new);
	}
}