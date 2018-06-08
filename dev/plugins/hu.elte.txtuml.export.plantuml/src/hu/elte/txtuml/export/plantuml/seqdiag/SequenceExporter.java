package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Currently responsible for exporting the message sending from the
 * SequenceDiagrams( {@code Sequence.send() } and {@code Sequence.fromActor() }})
 * 
 * @todo lifeline activation,deactivation, deletion, creation
 *
 */
public class SequenceExporter extends MethodInvocationExporter {

	public SequenceExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (super.validElement(curElement)) {
			String fullName = PlantUmlCompiler.getFullyQualifiedName((MethodInvocation) curElement);
			if (fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.send")
					|| fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor")) {
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
			compiler.activateLifeline(targetName);
			return true;
		}

		Expression sender = (Expression) curElement.arguments().get(0);
		String senderName = sender.toString();

		Expression target = (Expression) curElement.arguments().get(2);
		String targetName = target.toString();

		Expression signal = (Expression) curElement.arguments().get(1);
		String signalExpr = signal.resolveTypeBinding().getQualifiedName();

		compiler.activateLifeline(targetName);

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
