package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

public class LifelineDeactivator extends BaseSeqdiagExporter<Block> {

	public LifelineDeactivator(PlantUmlGenerator generator) {
		super(generator);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		ASTNode parent = curElement.getParent();
		if (parent instanceof MethodDeclaration) {
			if (((MethodDeclaration) parent).getName().toString().equals("run")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void preNext(Block curElement) {

	}

	@Override
	public void afterNext(Block curElement) {
		generator.deactivateAllLifelines();
	}
}
