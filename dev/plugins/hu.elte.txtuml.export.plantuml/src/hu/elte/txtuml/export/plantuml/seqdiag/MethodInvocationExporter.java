package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

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

	public static MethodInvocationExporter create(PlantUmlCompiler compiler, MethodInvocation curElement) {
		MessageSendExporter exp = new MessageSendExporter(compiler);
		if (exp.validElement(curElement)) {
			return exp;
		}
		return null;
	}
}
