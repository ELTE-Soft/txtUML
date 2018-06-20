package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for exporting the
 * {@link SequenceDiagram} and {@link Interaction} classes.
 */
public class InteractionExporter extends ExporterBase<TypeDeclaration> {

	public InteractionExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		return (curElement.getNodeType() == ASTNode.TYPE_DECLARATION)
				&& (((TypeDeclaration) curElement).getName().toString().equals(compiler.getSeqDiagramName()));
	}

	@Override
	public boolean preNext(TypeDeclaration curElement) {
		compiler.println("@startuml");
		return true;
	}

	@Override
	public void afterNext(TypeDeclaration curElement) {
		compiler.println("@enduml");
	}

}
