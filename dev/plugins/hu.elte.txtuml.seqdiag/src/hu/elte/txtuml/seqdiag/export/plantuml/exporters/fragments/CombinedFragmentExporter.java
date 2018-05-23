package hu.elte.txtuml.seqdiag.export.plantuml.exporters.fragments;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.seqdiag.export.plantuml.exporters.BaseSeqdiagExporter;
import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

/**
 * Base class for exporting a combined fragment
 * 
 * @param <T>
 */
public abstract class CombinedFragmentExporter<T extends ASTNode> extends BaseSeqdiagExporter<T> {

	public CombinedFragmentExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		int nodeType = curElement.getNodeType();

		if (nodeType == ASTNode.DO_STATEMENT || nodeType == ASTNode.ENHANCED_FOR_STATEMENT
				|| nodeType == ASTNode.WHILE_STATEMENT || nodeType == ASTNode.FOR_STATEMENT
				|| nodeType == ASTNode.IF_STATEMENT || nodeType == ASTNode.METHOD_DECLARATION) {

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

	@Override
	public void afterNext(T curElement) {
		compiler.println("end");
	}
}
