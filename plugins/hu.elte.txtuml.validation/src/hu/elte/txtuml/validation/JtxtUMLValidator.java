package hu.elte.txtuml.validation;

import java.io.IOException;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.eclipseutils.PackageUtils;
import hu.elte.txtuml.export.uml2.utils.ModelUtils;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.validation.visitors.ModelVisitor;

public class JtxtUMLValidator {

	private final NavigableSet<String> knownModels = new TreeSet<>();

	public JtxtUMLValidator(IJavaProject project) {
		CompilationUnit[] units;
		try {
			Stream<ICompilationUnit> stream = PackageUtils.findAllPackageFragmentsAsStream(project)
					.map(pf -> pf.getCompilationUnit(PackageUtils.PACKAGE_INFO)).filter(ICompilationUnit::exists);
			units = SharedUtils.parseICompilationUnitStream(stream, project);
		} catch (JavaModelException | IOException e) {
			// Cannot open package-info.java files.
			return;
		}

		Stream.of(units).filter(cu -> ModelUtils.findModelNameInTopPackage(cu).isPresent())
				.map(JtxtUMLValidator::getPackageName).forEach(knownModels::add);
	}

	public void validate(CompilationUnit unit, ProblemCollector problemCollector) {
		String packageName = getPackageName(unit);
		if (packageName == null) {
			return;
		}

		String floor = knownModels.floor(packageName);

		if (floor == null || !(packageName.equals(floor) || packageName.startsWith(floor + "."))) {
			return;
		}

		unit.accept(new ModelVisitor(problemCollector));
	}

	private static String getPackageName(CompilationUnit unit) {
		try {
			return unit.getPackage().resolveBinding().getName();
		} catch (NullPointerException e) {
			return null;
		}
	}

}
