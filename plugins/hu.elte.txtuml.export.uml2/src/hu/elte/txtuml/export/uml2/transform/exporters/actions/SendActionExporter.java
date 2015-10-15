package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.List;

public class SendActionExporter {

	@SuppressWarnings("unused")
	private final ExpressionExporter expressionExporter;

	public SendActionExporter(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public void export(List<Expr> args) {
		// TODO export send
	}
	
	/*
	@Override
	public void exportFromMethodInvocation(List<Expr> args) {

		Signal signalToSend = this.obtainSignalToSend(args);
		Type instanceType = this.obtainInstanceType(args);
		String instanceExpression = this.obtainInstanceExpression(args);

		SendObjectAction sendAction = (SendObjectAction) this.activity
				.createOwnedNode("send_" + signalToSend.getName() + "_to_"
						+ instanceExpression,
						UMLPackage.Literals.SEND_OBJECT_ACTION);

		createAndWireRequest(sendAction, args, signalToSend);
		createAndWireTarget(instanceType, instanceExpression, sendAction);

		this.methodBodyExporter.getBodyNode().getExecutableNodes()
				.add(sendAction);

	}

	private void createAndWireTarget(Type instanceType,
			String instanceExpression, SendObjectAction sendAction) {

		ValuePin target = (ValuePin) sendAction.createTarget(
				sendAction.getName() + "_target", instanceType,
				UMLPackage.Literals.VALUE_PIN);

		this.createAndAddOpaqueExpressionToValuePin(target, instanceExpression,
				instanceType);
	}

	private void createAndWireRequest(SendObjectAction sendAction,
			MethodInvocation methodInvocation, Signal signalToSend) {

		ValuePin request = (ValuePin) sendAction.createRequest(
				sendAction.getName() + "_request", signalToSend,
				UMLPackage.Literals.VALUE_PIN);

		Expression signalArg = (Expression) methodInvocation.arguments().get(1);
		String signalExpression = null;
		if (signalArg instanceof ClassInstanceCreation) {

			ClassInstanceCreation signalCreation = (ClassInstanceCreation) signalArg;

			Variable signalVar = exportSignalCreation(signalCreation,
					signalToSend);
			if (signalVar != null) {
				signalExpression = signalVar.getName();
			}

		} else {
			ExpressionVisitorOLD visitor = new ExpressionVisitorOLD();
			signalArg.accept(visitor);
			signalExpression = visitor.getExportedExpression();
		}

		this.createAndAddValueExpressionToValuePin(request, signalExpression,
				signalToSend);
	}

	private Type obtainInstanceType(MethodInvocation methodInvocation) {
		return obtainTypeOfNthArgument(methodInvocation, 1);
	}

	private Signal obtainSignalToSend(MethodInvocation methodInvocation) {
		return (Signal) obtainTypeOfNthArgument(methodInvocation, 2);
	}

	private String obtainInstanceExpression(MethodInvocation methodInvocation) {
		Expression expr = (Expression) methodInvocation.arguments().get(0);
		ExpressionVisitorOLD expressionVisitor = new ExpressionVisitorOLD();
		expr.accept(expressionVisitor);
		return expressionVisitor.getExportedExpression();
	}

	private Variable exportSignalCreation(ClassInstanceCreation signalCreation,
			Signal signal) {
		String variableName = "signal_"
				+ System.identityHashCode(signalCreation);
		Variable variable = this.activity.createVariable(variableName, signal);
		return variable;
	}
	*/
}
