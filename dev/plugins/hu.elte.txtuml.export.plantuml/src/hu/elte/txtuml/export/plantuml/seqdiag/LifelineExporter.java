package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

/**
 * 
 * @author Zoli
 * 
 *         Responsible for exporting the {@link ModelClass} lifelines from the
 *         SequenceDiagram
 *
 */
public class LifelineExporter extends BaseSeqdiagExporter<FieldDeclaration> {

	public LifelineExporter(PrintWriter targetFile, PlantUmlGenerator generator) {
		super(targetFile, generator);
	}

	@Override
	public boolean validElement(ASTNode curElement) {

		return true;
	}

	@Override
	public void preNext(FieldDeclaration curElement) {

		String participantName = curElement.fragments().get(0).toString();

		targetFile.println("participant " + participantName);
	}

	@Override
	public void afterNext(FieldDeclaration curElement) {

	}

}
