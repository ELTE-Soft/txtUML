package hu.elte.txtuml.export.plantuml.seqdiag;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for exporting
 * {@link ModelClass} lifelines from the user-given {@link SequenceDiagram}.
 */
public class LifelineExporter extends ExporterBase<FieldDeclaration> {

	public LifelineExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		return true;
	}

	/**
	 * Compiles lifelines in the order of their given position.
	 */
	@Override
	public boolean preNext(FieldDeclaration curElement) {
		List<?> modifiers = curElement.modifiers();
		Optional<Integer> maybePosition = modifiers.stream().filter(modifier -> modifier instanceof Annotation)
				.map(modifier -> (Annotation) modifier)
				.filter(annot -> annot.getTypeName().getFullyQualifiedName().equals("Position"))
				.map(annot -> (int) ((SingleMemberAnnotation) annot).getValue().resolveConstantExpressionValue()).findFirst();

		if (maybePosition.isPresent()) {
			int position = maybePosition.get();
			compiler.addLifeline(position, curElement);
		}
		return true;
	}

	@Override
	public void afterNext(FieldDeclaration curElement) {
	}

}
