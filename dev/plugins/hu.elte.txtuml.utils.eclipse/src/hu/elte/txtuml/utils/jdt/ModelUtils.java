package hu.elte.txtuml.utils.jdt;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;

import hu.elte.txtuml.api.model.Model;

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
