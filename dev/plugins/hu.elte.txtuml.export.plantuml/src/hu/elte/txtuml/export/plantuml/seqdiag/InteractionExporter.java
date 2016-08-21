package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

/**
 * 
 * @author Zoli
 *
 *
 *         The class responsible for exporting the {@link SequenceDiagram} and
 *         {@link Interaction} classes
 */
public class InteractionExporter extends BaseSeqdiagExporter<TypeDeclaration> {

	public InteractionExporter(PrintWriter targetFile, PlantUmlGenerator generator) {
		super(targetFile, generator);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.TYPE_DECLARATION)
			return true;
		else
			return false;
	}

	@Override
	public void preNext(TypeDeclaration curElement) {
		targetFile.println("@startuml");
	}

	@Override
	public void afterNext(TypeDeclaration curElement) {
		targetFile.println("@enduml");
	}
}
