package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.api.model.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * Instances of this class are responsible from obtaining a txtUML model from a
 * Java compilation unit.
 * 
 * @author Ádám Ancsin
 *
 */
public class ModelObtainer extends ASTVisitor {

	private final String packageName;
	private final CompilationUnit[] compilationUnits;
	private Optional<String> modelName = Optional.empty();

	public ModelObtainer(String packageName, CompilationUnit[] compilationUnits) {
		this.packageName = packageName;
		this.compilationUnits = compilationUnits;
	}

	public Optional<String> getModelName() {
		for (CompilationUnit cu : compilationUnits) {
			cu.accept(this);
		}
		return modelName;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		List<IAnnotationBinding> annots = Stream
				.of(node.resolveBinding().getAnnotations())
				.filter(a -> a.getAnnotationType().getQualifiedName()
						.equals(Model.class.getCanonicalName()))
				.collect(Collectors.toList());

		if (annots.size() == 1) {
			Optional<String> name = Stream
					.of(annots.get(0).getDeclaredMemberValuePairs())
					.filter(p -> p.getName().equals("value"))
					.map(p -> (String) p.getValue()).findAny();
			if (name.isPresent()) {
				modelName = name;
			} else {
				modelName = Optional.of(packageName);
			}
		}

		return false;
	}

}