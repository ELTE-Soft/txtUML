package hu.elte.txtuml.utils.eclipse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.jdt.ModelUtils;
import hu.elte.txtuml.utils.jdt.SharedUtils;

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
					.of(pFragment.getCompilationUnit(PackageUtils.PACKAGE_INFO));

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

	public static IBaseLabelProvider getPostQualifiedLabelProvider() {
		return new DelegatingStyledCellLabelProvider(new JavaElementLabelProvider(
				JavaElementLabelProvider.SHOW_POST_QUALIFIED | JavaElementLabelProvider.SHOW_SMALL_ICONS)) {
			@Override
			protected StyledString getStyledText(Object element) {
				String nameWithQualifier = getStyledStringProvider().getStyledText(element).getString() + " ";
				int separatorIndex = nameWithQualifier.indexOf('-');

				if (separatorIndex == -1)
					return new StyledString(nameWithQualifier);

				StyledString name = new StyledString(nameWithQualifier.substring(0, separatorIndex));
				String qualifier = nameWithQualifier.substring(separatorIndex);
				return name.append(new StyledString(qualifier, StyledString.QUALIFIER_STYLER));
			};
		};
	}

	public static String[][] resolveType(IType context, Object typeName) {
		try {
			return context.resolveType((String) typeName);
		} catch (JavaModelException e) {
			return new String[][] {};
		}
	}

	public static Pair<String, String> getModelPackage(String packageName) {
		try {
			Optional<IJavaProject> project = Stream.of(ResourcesPlugin.getWorkspace().getRoot().getProjects())
					.map(pr -> {
						try {
							return ProjectUtils.findJavaProject(pr.getName());
						} catch (NotFoundException e) {
							return null;
						}
					}).filter(Objects::nonNull).filter(pr -> {
						IPackageFragment[] pf = null;
						try {
							pf = PackageUtils.findPackageFragments(pr, packageName);
						} catch (JavaModelException e) {
							return false;
						}
						return pf.length > 0;
					}).findFirst();

			Stream<ICompilationUnit> modelCU = PackageUtils.findAllPackageFragmentsAsStream(project.get())
					.filter(p -> packageName.startsWith(p.getElementName() + ".")
							|| packageName.equals(p.getElementName()))
					.map(pf -> pf.getCompilationUnit(PackageUtils.PACKAGE_INFO)).filter(ICompilationUnit::exists);

			Optional<String> topPackageName = Stream.of(SharedUtils.parseICompilationUnitStream(modelCU, project.get()))
					.map(CompilationUnit::getPackage).filter(Objects::nonNull).map(PackageDeclaration::resolveBinding)
					.filter(Objects::nonNull).filter(pb -> ModelUtils.findModelNameInTopPackage(pb).isPresent())
					.map(IPackageBinding::getName).findFirst();

			if (topPackageName.isPresent()) {
				return Pair.of(topPackageName.get(), project.get().getElementName());
			}
		} catch (JavaModelException | IOException e) {
		}
		return Pair.of("", "");
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
