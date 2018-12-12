package hu.elte.txtuml.seqdiag.export.plantuml.exporters;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;
import hu.elte.txtuml.seqdiag.export.plantuml.exporters.fragments.ParFragmentExporter;

/**
 * Base exporter for all {@code MethodInvocation} handlers ({@link Sequence}
 * calls).
 */
public abstract class MethodInvocationExporter extends ExporterBase<MethodInvocation> {

	public MethodInvocationExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		return curElement.getNodeType() == ASTNode.METHOD_INVOCATION;
	}

	/**
	 * Creates the appropriate exporter implementation.
	 * 
	 * @param <T>
	 *            The node type which is parsed by the exporter.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ASTNode> ExporterBase<T> createExporter(T curElement, PlantUmlCompiler compiler) {
        if (curElement.getNodeType() != ASTNode.METHOD_INVOCATION) {
            return null;
        }

        if (ExporterUtils.isCommunication(curElement)) {
            return (ExporterBase<T>) new SequenceExporter(compiler);
        } else if (ExporterUtils.isParFragment(curElement)) {
            return (ExporterBase<T>) new ParFragmentExporter(compiler);
        }
        return (ExporterBase<T>) new UserMethodInvocationExporter(compiler);
	}

}
