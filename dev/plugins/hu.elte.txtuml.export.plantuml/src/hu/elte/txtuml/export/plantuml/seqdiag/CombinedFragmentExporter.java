package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

public abstract class CombinedFragmentExporter<T extends ASTNode> extends BaseSeqdiagExporter<T> {

	public CombinedFragmentExporter(PlantUmlGenerator generator) {
		super(generator);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		int nodeType = curElement.getNodeType();

		if (nodeType == ASTNode.WHILE_STATEMENT || nodeType == ASTNode.FOR_STATEMENT || nodeType == ASTNode.IF_STATEMENT
				|| nodeType == ASTNode.METHOD_DECLARATION) {

			if (nodeType == ASTNode.METHOD_DECLARATION) {
				MethodDeclaration decl = (MethodDeclaration) curElement;

				if (decl.getName().equals("initialize") || decl.getName().equals("run")) {
					return false;
				}
			}

			return true;
		}

		return false;
	}
}
