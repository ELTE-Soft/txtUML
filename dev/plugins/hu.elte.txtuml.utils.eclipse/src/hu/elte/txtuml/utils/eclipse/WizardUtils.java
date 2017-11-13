package hu.elte.txtuml.utils.eclipse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.jdt.ModelUtils;

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

	/**
	 * @return an empty optional if the given type is not annotated, otherwise
	 *         the name of the model package and its java project respectively,
	 *         which contains the types of the given annotation
	 */
	public static Optional<Pair<String, String>> getModelByAnnotations(IType annotatedType) {
		try {
			List<String> referencedProjects = new ArrayList<>(
					Arrays.asList(annotatedType.getJavaProject().getRequiredProjectNames()));
			referencedProjects.add(annotatedType.getJavaProject().getElementName());
			for (IAnnotation annot : annotatedType.getAnnotations()) {
				List<Object> annotValues = Stream.of(annot.getMemberValuePairs())
						.filter(mvp -> mvp.getValueKind() == IMemberValuePair.K_CLASS)
						.flatMap(mvp -> Stream.of(mvp.getValue())).collect(Collectors.toList());

				for (Object val : annotValues) {
					List<Object> annotations = new ArrayList<>();
					if (val instanceof String) {
						annotations.add(val);
					} else {
						annotations.addAll(Arrays.asList((Object[]) val));
					}

					for (Object v : annotations) {
						String[][] resolvedTypes = resolveType(annotatedType, (String) v);
						List<String[]> resolvedTypeList = new ArrayList<>(Arrays.asList(resolvedTypes));
						for (String[] type : resolvedTypeList) {
							Optional<Pair<String, String>> model = ModelUtils.getModelOf(type[0], referencedProjects);
							if (model.isPresent()) {
								return model;
							}
						}
					}
				}
			}
		} catch (JavaModelException | NoSuchElementException e) {
		}

		return Optional.empty();
	}

	/**
	 * @return an empty optional if the given type does not have any field,
	 *         otherwise the name of the model package and its java project
	 *         respectively, which contains the types of the given diagramType
	 */
	public static Optional<Pair<String, String>> getModelByFields(IType diagramType) {
		try {
			List<String> referencedProjects = new ArrayList<>(
					Arrays.asList(diagramType.getJavaProject().getRequiredProjectNames()));
			referencedProjects.add(diagramType.getJavaProject().getElementName());

			for (IField field : diagramType.getFields()) {
				String typeSignature = field.getTypeSignature();
				String[][] resolvedTypes = resolveType(diagramType,
						typeSignature.substring(1, typeSignature.length() - 1));
				List<String[]> resolvedTypeList = new ArrayList<>(Arrays.asList(resolvedTypes));

				for (String[] type : resolvedTypeList) {
					Optional<Pair<String, String>> model = ModelUtils.getModelOf(type[0], referencedProjects);
					if (model.isPresent()) {
						return model;
					}
				}
			}
		} catch (JavaModelException | NoSuchElementException e) {
		}

		return Optional.empty();
	}

	private static String[][] resolveType(IType context, String typeName) {
		try {
			return context.resolveType(typeName);
		} catch (JavaModelException e) {
			return new String[][] {};
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
