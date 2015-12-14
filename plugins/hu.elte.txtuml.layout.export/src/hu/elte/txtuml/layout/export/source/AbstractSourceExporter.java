package hu.elte.txtuml.layout.export.source;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.jdt.ModelUtils;
import hu.elte.txtuml.utils.jdt.SharedUtils;
import hu.elte.txtuml.utils.platform.NotFoundException;
import hu.elte.txtuml.utils.platform.PackageUtils;
import hu.elte.txtuml.utils.platform.ProjectUtils;

abstract class AbstractSourceExporter implements SourceExporter {

	@Override
	public ModelId getModelOf(Class<?> element, ElementExporter elementExporter) throws ElementExportationException {
		try {
			Package ownPackage = element.getPackage();

			if (ownPackage.getAnnotation(Model.class) != null) {
				return new ModelIdImpl(ownPackage.getName());
			}
			String ownPackageName = ownPackage.getName();

			IJavaProject javaProject = ProjectUtils.findJavaProject(elementExporter.getSourceProjectName());

			Stream<ICompilationUnit> stream = PackageUtils.findAllPackageFragmentsAsStream(javaProject)
					.filter(p -> ownPackageName.startsWith(p.getElementName() + "."))
					.map(pf -> pf.getCompilationUnit(PackageUtils.PACKAGE_INFO)).filter(ICompilationUnit::exists);

			String topPackageName = Stream.of(SharedUtils.parseICompilationUnitStream(stream, javaProject))
					.map(CompilationUnit::getPackage).filter(Objects::nonNull).map(PackageDeclaration::resolveBinding)
					.filter(Objects::nonNull).filter(pb -> ModelUtils.findModelNameInTopPackage(pb).isPresent())
					.map(IPackageBinding::getName).findFirst().get();

			return new ModelIdImpl(topPackageName);

		} catch (NotFoundException | JavaModelException | IOException | NoSuchElementException e) {
			e.printStackTrace();
			throw new ElementExportationException();
		}
	}

	@Override
	public abstract void exportImpliedLinks(ModelId modelId, ElementExporter elementExporter);

}
