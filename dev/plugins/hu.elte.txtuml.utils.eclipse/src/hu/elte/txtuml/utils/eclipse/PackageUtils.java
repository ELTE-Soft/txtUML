package hu.elte.txtuml.utils.eclipse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import hu.elte.txtuml.utils.Sneaky;

public final class PackageUtils {

	public static final String PACKAGE_INFO = "package-info.java";

	/**
	 * Also returns subpackages of the specified package.
	 */
	public static IPackageFragment[] findPackageFragments(String projectName, String packageName)
			throws JavaModelException, NotFoundException {
		return findPackageFragments(ProjectUtils.findJavaProject(projectName), packageName);
	}

	/**
	 * Also returns subpackages of the specified package.
	 */
	public static IPackageFragment[] findPackageFragments(IJavaProject javaProject, String packageName)
			throws JavaModelException {

		// Sneaky.<JavaModelException> Throw();
		return findAllPackageFragmentsAsStream(javaProject).filter(
				pf -> pf.getElementName().equals(packageName) || pf.getElementName().startsWith(packageName + "."))
				.toArray(IPackageFragment[]::new);

	}

	public static Stream<IPackageFragment> findAllPackageFragmentsAsStream(IJavaProject javaProject)
			throws JavaModelException {

		// Sneaky.<JavaModelException> Throw();
		// TODO check if explicit type parameters can be omitted > Neon.2
		return getPackageFragmentRootsAsStream(javaProject)
				.flatMap(Sneaky.<IPackageFragmentRoot, Stream<IJavaElement>, JavaModelException>unchecked(
						pfr -> Stream.of(pfr.getChildren())))
				.map(pf -> (IPackageFragment) pf);

	}

	public static Stream<IPackageFragmentRoot> getPackageFragmentRootsAsStream(IJavaProject javaProject)
			throws JavaModelException {

		return Stream.of(javaProject.getRawClasspath()).filter(e -> e.getEntryKind() == IClasspathEntry.CPE_SOURCE)
				.flatMap(e -> Stream.of(javaProject.findPackageFragmentRoots(e)));

	}

	public static List<IPackageFragmentRoot> getPackageFragmentRoots(IJavaProject javaProject)
			throws JavaModelException {

		return getPackageFragmentRootsAsStream(javaProject).collect(Collectors.toList());

	}

	private PackageUtils() {
	}

}
