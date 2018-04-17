package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for deactivating all remaining
 * active lifelines at the end of the sequence diagram description.
 */
public class LifelineDeactivator extends ExporterBase<Block> {

	public LifelineDeactivator(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		ASTNode parent = curElement.getParent();
		if (parent instanceof MethodDeclaration) {
			return ((MethodDeclaration) parent).getName().toString().equals("run");
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
