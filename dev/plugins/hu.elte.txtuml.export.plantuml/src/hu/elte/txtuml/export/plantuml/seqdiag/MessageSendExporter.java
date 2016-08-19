package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MessageSendExporter extends BaseSeqdiagExporter<MethodInvocation> {

	public MessageSendExporter(PrintWriter targetFile) {
		super(targetFile);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.METHOD_INVOCATION) {
			MethodInvocation invoc = (MethodInvocation) curElement;
			String QualifiedTypeName = invoc.resolveMethodBinding().getDeclaringClass().getQualifiedName();
			String MethodName = invoc.getName().toString();
			if (MethodName.equals("send") && QualifiedTypeName.equals("hu.elte.txtuml.api.model.seqdiag.Action")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void preNext(MethodInvocation curElement) {

		Expression sender = (Expression) curElement.arguments().get(0);
		String senderName = sender.toString();

		Expression target = (Expression) curElement.arguments().get(2);
		String targetName = target.toString();

		Expression signal = (Expression) curElement.arguments().get(1);
		String signalExpr = signal.resolveTypeBinding().getQualifiedName();

		targetFile.println("activate " + senderName);
		targetFile.println(senderName + "->" + targetName + " : " + signalExpr);
	}

	@Override
	public void afterNext(MethodInvocation curElement) {
		Expression sender = (Expression) curElement.arguments().get(0);
		String senderName = sender.toString();

		targetFile.println("deactivate " + senderName);
	}

}
