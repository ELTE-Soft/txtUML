package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import java.util.List;

import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public class SendActionExporter {

	private final ExpressionExporter<? extends ActivityNode> expressionExporter;

	public SendActionExporter(ExpressionExporter<? extends ActivityNode> expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public void export(List<Expr> args) throws ExportException {
		args.forEach(Expr::evaluate);

		Signal signalToSend = this.obtainSignalToSend(args);
		Expr instanceExpression = args.get(0);

		String sendActionName = "send_" + signalToSend.getName() + "_to_" + instanceExpression.getName();

		SendObjectAction sendAction = (SendObjectAction) expressionExporter.createAndAddNode(sendActionName,
				UMLPackage.Literals.SEND_OBJECT_ACTION);

		createAndWireRequest(sendAction, signalToSend, args);
		createAndWireTarget(sendAction, instanceExpression);
	}

	private void createAndWireTarget(SendObjectAction sendAction, Expr instanceExpression) {

		Type instanceType = instanceExpression.getType();

		InputPin targetNode = (InputPin) sendAction.createTarget(sendAction.getName() + "_target", instanceType,
				UMLPackage.Literals.INPUT_PIN);

		ObjectNode instanceNode = instanceExpression.getObjectNode();
		expressionExporter.createObjectFlowBetweenActivityNodes(instanceNode, targetNode);
	}

	private void createAndWireRequest(SendObjectAction sendAction, Signal signalToSend, List<Expr> args) {

		InputPin requestNode = (InputPin) sendAction.createRequest(sendAction.getName() + "_request", signalToSend,
				UMLPackage.Literals.VALUE_PIN);

		Expr signalExpression = args.get(1);
		ObjectNode instanceNode = signalExpression.getObjectNode();
		expressionExporter.createObjectFlowBetweenActivityNodes(instanceNode, requestNode);
	}

	private Signal obtainSignalToSend(List<Expr> args) throws ExportException {
		try {
			return (Signal) args.get(1).getType();
		} catch (ClassCastException e) {
			throw new ExportException("Failed to export send signal action. 2nd argument is not of type Signal.");
		}
	}
}
