package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * 
 * @author Zoli
 *
 *         Responsible for exporting the message sending from the
 *         SequenceDiagrams( {@code API.send() } and {@code Action.send() }})
 *
 */
public class MessageSendExporter extends MethodInvocationExporter {

	public MessageSendExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (super.validElement(curElement)) {
			String fullName = getMethodFullyQualifiedName((MethodInvocation) curElement);
			if (fullName.equals("hu.elte.txtuml.api.model.seqdiag.Action.send")
					|| fullName.equals("hu.elte.txtuml.api.model.seqdiag.API.send")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean preNext(MethodInvocation curElement) {

		if (curElement.arguments().size() == 2) {
			Expression target = (Expression) curElement.arguments().get(1);
			String targetName = target.toString();

			if (!compiler.lifelineIsActive(targetName)) {
				compiler.activateLifeline(targetName);
			}
			return true;
		}

		Expression sender = (Expression) curElement.arguments().get(0);
		String senderName = sender.toString();

		Expression target = (Expression) curElement.arguments().get(2);
		String targetName = target.toString();

		Expression signal = (Expression) curElement.arguments().get(1);
		String signalExpr = signal.resolveTypeBinding().getQualifiedName();

		if (!compiler.lifelineIsActive(targetName)) {
			compiler.activateLifeline(targetName);
		}

		compiler.println(senderName + "->" + targetName + " : " + signalExpr);

		return true;
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
