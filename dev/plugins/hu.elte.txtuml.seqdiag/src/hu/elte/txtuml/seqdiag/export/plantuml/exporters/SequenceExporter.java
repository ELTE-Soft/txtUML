package hu.elte.txtuml.seqdiag.export.plantuml.exporters;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for exporting the message
 * sending from the SequenceDiagrams ({@code Sequence.assertSend()} and
 * {@code Sequence.fromActor()})
 */
public class SequenceExporter extends MethodInvocationExporter {

	public SequenceExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (super.validElement(curElement)) {
			String fullName = ExporterUtils.getFullyQualifiedName((MethodInvocation) curElement);
			return fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend")
					|| fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor");
		}
		return false;
	}

	@Override
	public boolean preNext(MethodInvocation curElement) {
		// Sequence.fromActor call
		if (curElement.arguments().size() == 2) {
			return true;
		}

		// Sequence.assertSend call
		Expression sender = (Expression) curElement.arguments().get(0);
		String senderName = sender.toString();

		Expression target = (Expression) curElement.arguments().get(2);
		String targetName = target.toString();

		Expression signal = (Expression) curElement.arguments().get(1);
		String signalExpr = signal.resolveTypeBinding().getQualifiedName();

		List<String> lifelineNames = compiler.getLifelineNames();
		if (lifelineNames.contains(senderName) || lifelineNames.contains(targetName)) {
			compiler.println(senderName + "->" + targetName + " : " + signalExpr);
		}
		return true;
	}

	@Override
	public void afterNext(MethodInvocation curElement) {
	}

}
