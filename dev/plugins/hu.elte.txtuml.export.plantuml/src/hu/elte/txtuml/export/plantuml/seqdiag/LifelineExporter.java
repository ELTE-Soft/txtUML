package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * 
 * @author Zoli
 * 
 *         Responsible for exporting the {@link ModelClass} lifelines from the
 *         SequenceDiagram
 *
 */
public class LifelineExporter extends BaseSeqdiagExporter<FieldDeclaration> {

	public LifelineExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {

		return true;
	}

	@Override
	public boolean preNext(FieldDeclaration curElement) {

		String participantName = curElement.fragments().get(0).toString();

		compiler.println("participant " + participantName);
		return true;
	}

	@Override
	public void afterNext(FieldDeclaration curElement) {

	}
}
