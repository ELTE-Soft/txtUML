package hu.elte.txtuml.seqdiag.export.plantuml.exporters.fragments;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.seqdiag.export.plantuml.exporters.ExporterBase;
import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

/**
 * Base class for exporting a combined fragment.
 * 
 * @param <T>
 *            Node type which is parsed by the exporter.
 */
public abstract class CombinedFragmentExporter<T extends ASTNode> extends ExporterBase<T> {

	public CombinedFragmentExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		int nodeType = curElement.getNodeType();
		return nodeType == ASTNode.DO_STATEMENT || nodeType == ASTNode.ENHANCED_FOR_STATEMENT
				|| nodeType == ASTNode.WHILE_STATEMENT || nodeType == ASTNode.FOR_STATEMENT
				|| nodeType == ASTNode.IF_STATEMENT || nodeType == ASTNode.METHOD_INVOCATION;
	}

	@Override
	public void afterNext(T curElement) {
		compiler.println("end");
	}

}
