package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

public class MessageSendExporter extends BaseSeqdiagExporter<MethodInvocation> {

	public MessageSendExporter(PrintWriter targetFile, PlantUmlGenerator generator) {
		super(targetFile, generator);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.METHOD_INVOCATION) {
			MethodInvocation invoc = (MethodInvocation) curElement;
			String QualifiedTypeName = invoc.resolveMethodBinding().getDeclaringClass().getQualifiedName();
			String MethodName = invoc.getName().toString();
			if (MethodName.equals("send") && (QualifiedTypeName.equals("hu.elte.txtuml.api.model.seqdiag.Action")
					|| QualifiedTypeName.equals("hu.elte.txtuml.api.model.API"))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void preNext(MethodInvocation curElement) {

		if (curElement.arguments().size() == 2) {
			Expression target = (Expression) curElement.arguments().get(1);
			String targetName = target.toString();

			if (!generator.lifelineIsActive(targetName)) {
				generator.activateLifeline(targetName);
			}
			return;
		}

		Expression sender = (Expression) curElement.arguments().get(0);
		String senderName = sender.toString();

		Expression target = (Expression) curElement.arguments().get(2);
		String targetName = target.toString();

		Expression signal = (Expression) curElement.arguments().get(1);
		String signalExpr = signal.resolveTypeBinding().getQualifiedName();

		if (!generator.lifelineIsActive(targetName)) {
			generator.activateLifeline(targetName);
		}

		targetFile.println(senderName + "->" + targetName + " : " + signalExpr);
	}

	@Override
	public void afterNext(MethodInvocation curElement) {
		
	}

}
