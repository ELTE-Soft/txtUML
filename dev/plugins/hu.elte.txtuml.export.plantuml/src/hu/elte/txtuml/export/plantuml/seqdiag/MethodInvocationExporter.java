package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Base exporter for all MethodInvocation handlers(Actions and
 * fragmentInvocations)
 */
public abstract class MethodInvocationExporter extends BaseSeqdiagExporter<MethodInvocation> {

	public MethodInvocationExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.METHOD_INVOCATION) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ASTNode> BaseSeqdiagExporter<T> createExporter(ASTNode curElement,
			PlantUmlCompiler compiler) {
		MethodInvocationExporter exp = new SequenceExporter(compiler);
		if (exp.validElement(curElement)) {
			return (BaseSeqdiagExporter<T>) exp;
		} else {
			return (BaseSeqdiagExporter<T>) (new FragmentInvocationExporter(compiler));
		}
	}
}
