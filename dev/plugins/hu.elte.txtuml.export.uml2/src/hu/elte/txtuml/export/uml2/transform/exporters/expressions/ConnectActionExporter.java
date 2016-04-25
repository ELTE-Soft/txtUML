package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import java.util.List;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.TypeLiteralExpr;

public class ConnectActionExporter {

	private ExpressionExporter<?> expressionExporter;

	public ConnectActionExporter(ExpressionExporter<?> expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public void export(IMethodBinding binding, Expr expression, List<Expr> args) {
		args.forEach(Expr::evaluate);

		Connector connector = getConnector(binding, args);
		LinkAction linkAction = (LinkAction) expressionExporter.createAndAddNode("connect",
				UMLPackage.Literals.CREATE_LINK_ACTION);
		if (args.size() == 4) {
			// assembly connector
			addEndToLinkAction(linkAction, connector, args.get(1), 0);
			addEndToLinkAction(linkAction, connector, args.get(3), 1);
		} else {
		    // delegation connector
			addEndToLinkAction(linkAction, connector, args.get(0), 0);
			addEndToLinkAction(linkAction, connector, args.get(2), 1);
		}
	}

	private void addEndToLinkAction(LinkAction linkAction, Connector connector, Expr expr, int index) {
		LinkEndData end = linkAction.createEndData();
		InputPin endPin = (InputPin) linkAction.createInputValue(linkAction.getName() + "_end" + index + "input",
				expr.getType(), UMLPackage.Literals.INPUT_PIN);
		end.setValue(endPin);
		expressionExporter.createObjectFlowBetweenActivityNodes(expr.getOutputPin(), end.getValue());
	}

	protected Connector getConnector(IMethodBinding binding, List<Expr> args) {
		Expr marker = (args.size() == 3) ? args.get(1) : args.get(0);
		TypeLiteralExpr markerArg = (TypeLiteralExpr) marker;
		return (Connector) expressionExporter.getTypeExporter().exportType(markerArg.getBinding().getDeclaringClass());
	}
}
