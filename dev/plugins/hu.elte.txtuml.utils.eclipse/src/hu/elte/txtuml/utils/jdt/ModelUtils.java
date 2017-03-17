package hu.elte.txtuml.utils.jdt;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.PackageUtils;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;

public class ModelUtils {

	/**
	 * @return an empty optional if the given package binding is not the top
	 *         package of a JtxtUML model; otherwise an optional of the name of
	 *         the model (the name is the name of the package if not specified)
	 */
	public static Optional<String> findModelNameInTopPackage(IPackageBinding packageBinding) {
		List<IAnnotationBinding> annots = Stream.of(packageBinding.getAnnotations())
				.filter(a -> a.getAnnotationType().getQualifiedName().equals(Model.class.getCanonicalName()))
				.collect(Collectors.toList());

		if (annots.size() == 1) {
			Optional<String> name = Stream.of(annots.get(0).getDeclaredMemberValuePairs())
					.filter(p -> p.getName().equals("value")).map(p -> (String) p.getValue()).findAny();
			if (name.isPresent()) {
				return name;
			} else {
				return Optional.of(packageBinding.getName());
			}
		}

		return Optional.empty();
	}

	/**
	 * @return an empty optional if the package of the given compilation unit is
	 *         not the top package of a JtxtUML model; otherwise an optional of
	 *         the name of the model (the name is the name of the package if not
	 *         specified)
	 */
	public static Optional<String> findModelNameInTopPackage(CompilationUnit cu) {
		try {
			return findModelNameInTopPackage(getPackageBinding(cu));
		} catch (NullPointerException e) {
			return Optional.empty();
		}
	}

	/**
	 * @param packageName
	 *            the package name
	 * @param javaProjectNames
	 *            the java project names which might contain the package
	 * @return an empty optional if the given package name (of a type) is not in
	 *         a txtUML model, otherwise the name of the model package and its
	 *         java project respectively, which contains the given type
	 */
	public static Optional<Pair<String, String>> getModelOf(String packageName, List<String> javaProjectNames) {
		try {
			List<IJavaProject> projectsContainingPackage = ProjectUtils.getAllJavaProjectsOfWorkspace().stream()
					.filter(pr -> {
						if (!javaProjectNames.contains(pr.getElementName())) {
							return false;
						}
						IPackageFragment[] pf = null;
						try {
							pf = PackageUtils.findPackageFragments(pr, packageName);
						} catch (JavaModelException e) {
							return false;
						}
						return pf.length > 0;
					}).collect(Collectors.toList());

			for (IJavaProject project : projectsContainingPackage) {
				Stream<ICompilationUnit> modelCU = PackageUtils.findAllPackageFragmentsAsStream(project)
						.filter(p -> packageName.startsWith(p.getElementName() + ".")
								|| packageName.equals(p.getElementName()))
						.map(pf -> pf.getCompilationUnit(PackageUtils.PACKAGE_INFO)).filter(ICompilationUnit::exists);

				Optional<String> topPackageName = Stream.of(SharedUtils.parseICompilationUnitStream(modelCU, project))
						.map(CompilationUnit::getPackage).filter(Objects::nonNull)
						.map(PackageDeclaration::resolveBinding).filter(Objects::nonNull)
						.filter(pb -> ModelUtils.findModelNameInTopPackage(pb).isPresent())
						.map(IPackageBinding::getName).findFirst();

				if (topPackageName.isPresent()) {
					return Optional.of(Pair.of(topPackageName.get(), project.getElementName()));
				}
			}
		} catch (JavaModelException | IOException e) {
		}
		return Optional.empty();
	}

	private static IPackageBinding getPackageBinding(CompilationUnit cu) {
		try {
			return cu.getPackage().resolveBinding();
		} catch (NullPointerException e) {
			return null;
		}
	}

	private ModelUtils() {
	}

}
