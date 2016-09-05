package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * 
 * @author Zoli
 *
 *
 *         The class responsible for exporting the {@link SequenceDiagram} and
 *         {@link Interaction} classes
 */
public class InteractionExporter extends BaseSeqdiagExporter<TypeDeclaration> {

	public InteractionExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.TYPE_DECLARATION)
			return true;
		else
			return false;
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
