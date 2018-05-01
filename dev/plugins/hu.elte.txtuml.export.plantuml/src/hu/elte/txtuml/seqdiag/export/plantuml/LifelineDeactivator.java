package hu.elte.txtuml.seqdiag.export.plantuml;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

/**
 * Class responsible for deactivating all remaining active lifelines
 */
public class LifelineDeactivator extends BaseSeqdiagExporter<Block> {

	public LifelineDeactivator(PlantUmlCompiler compiler) {
		super(compiler);
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
	public boolean preNext(Block curElement) {
		return true;
	}

	@Override
	public void afterNext(Block curElement) {
		compiler.deactivateAllLifelines();
	}
}
