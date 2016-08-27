package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

/**
 * 
 * @author Zoli
 *
 *         Responsible for exporting the message sending from the
 *         SequenceDiagrams( {@code API.send() } and {@code Action.send() }})
 *
 */
public class MessageSendExporter extends BaseSeqdiagExporter<MethodInvocation> {

	public MessageSendExporter(PlantUmlGenerator generator) {
		super(generator);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.METHOD_INVOCATION) {
			String fullName = getMethodFullyQualifiedName((MethodInvocation)curElement);
			if (fullName.equals("hu.elte.txtuml.api.model.seqdiag.Action.send")
					|| fullName.equals("hu.elte.txtuml.api.model.seqdiag.API.send")) {
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

	protected String getMethodFullyQualifiedName(MethodInvocation inv) {
		String QualifiedTypeName = inv.resolveMethodBinding().getDeclaringClass().getQualifiedName();
		String MethodName = inv.getName().toString();

		return QualifiedTypeName + "." + MethodName;
	}
}
