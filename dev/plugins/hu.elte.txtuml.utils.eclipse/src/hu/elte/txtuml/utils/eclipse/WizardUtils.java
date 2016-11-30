package hu.elte.txtuml.utils.eclipse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import hu.elte.txtuml.api.model.Model;

public class WizardUtils {

	public static List<IType> getTypesBySuperclass(IPackageFragment packageFragment, Class<?>... superClasses) {
		List<IType> typesWithGivenSuperclass = new ArrayList<>();

		ICompilationUnit[] compilationUnits;
		try {
			compilationUnits = packageFragment.getCompilationUnits();
		} catch (JavaModelException ex) {
			return Collections.emptyList();
		}

		for (ICompilationUnit cUnit : compilationUnits) {
			IType[] types = null;
			try {
				types = cUnit.getAllTypes();
			} catch (JavaModelException e) {
				continue;
			}

			Stream.of(types).filter(type -> {
				try {
					String typeSuperclassName = type.getSuperclassName();
					int indexOfTypeParam = typeSuperclassName.indexOf("<");
					if (indexOfTypeParam != -1) {
						typeSuperclassName = typeSuperclassName.substring(0, indexOfTypeParam);
					}
					final String superClassName = typeSuperclassName;
					return Stream.of(superClasses).anyMatch(superClass -> isImportedNameResolvedTo(cUnit,
							superClassName, superClass.getCanonicalName()));
				} catch (JavaModelException | NullPointerException ex) {
					return false;
				}
			}).forEach(type -> typesWithGivenSuperclass.add(type));
		}
		return typesWithGivenSuperclass;
	}

	public static List<IPackageFragment> getModelPackages(List<IPackageFragment> packageFragments) {
		List<IPackageFragment> modelPackages = new ArrayList<>();
		for (IPackageFragment pFragment : packageFragments) {
			Optional<ICompilationUnit> foundPackageInfoCompilationUnit = Optional
					.of(pFragment.getCompilationUnit("package-info.java"));

			if (!foundPackageInfoCompilationUnit.isPresent() || !foundPackageInfoCompilationUnit.get().exists()) {
				continue;
			}

			ICompilationUnit packageInfoCompilationUnit = foundPackageInfoCompilationUnit.get();

			try {
				for (IPackageDeclaration packDecl : packageInfoCompilationUnit.getPackageDeclarations()) {
					for (IAnnotation annot : packDecl.getAnnotations()) {
						boolean isModelPackage = isImportedNameResolvedTo(packageInfoCompilationUnit,
								annot.getElementName(), Model.class.getCanonicalName());

						if (isModelPackage) {
							modelPackages.add(pFragment);
							break;
						}
					}
				}
			} catch (JavaModelException ex) {
				return Collections.emptyList();
			}
		}
		return modelPackages;
	}

	public static boolean containsClassesWithSuperTypes(IJavaProject javaProject, Class<?>... superClasses) {
		try {
			return PackageUtils.findAllPackageFragmentsAsStream(javaProject)
					.anyMatch(pf -> !getTypesBySuperclass(pf, superClasses).isEmpty());
		} catch (JavaModelException ex) {
			return false;
		}
	}

	private static boolean isImportedNameResolvedTo(ICompilationUnit compilationUnit, String elementName,
			String qualifiedName) {
		if (!qualifiedName.endsWith(elementName)) {
			return false;
		}
		int lastSection = qualifiedName.lastIndexOf(".");
		String pack = qualifiedName.substring(0, lastSection);
		return (compilationUnit.getImport(qualifiedName).exists() || compilationUnit.getImport(pack + ".*").exists());
	}

}
