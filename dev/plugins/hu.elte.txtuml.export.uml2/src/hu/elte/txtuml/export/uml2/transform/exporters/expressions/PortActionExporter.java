package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import java.util.List;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.TypeLiteralExpr;

public class PortActionExporter {

	private ExpressionExporter<?> expressionExporter;

	public PortActionExporter(ExpressionExporter<?> expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public Expr export(IMethodBinding binding, Expr base, List<Expr> args) {
		TypeLiteralExpr selectorExpr = (TypeLiteralExpr) args.get(0);
		ITypeBinding selectorType = selectorExpr.getBinding().getSuperclass().getTypeArguments()[0];

		Port port = getPort(args);
		
		String nodeName = base.getName() + "->" + selectorExpr.getName();
		base.evaluate();
		ReadLinkAction rla = (ReadLinkAction) expressionExporter.createAndAddNode(nodeName,
				UMLPackage.Literals.READ_LINK_ACTION);
		OutputPin res = rla.createResult("res" + rla.getName(),
				expressionExporter.getTypeExporter().exportType(selectorType));

		addEndToLinkAction(rla, port, base, 1);
		return Expr.ofPin(res, res.getName());
	}

	private Port getPort(List<Expr> args) {
		TypeLiteralExpr selector = (TypeLiteralExpr) args.get(0);
		Class cls = (Class) expressionExporter.getTypeExporter().exportType(selector.getBinding().getDeclaringClass());
		for (Port port : cls.getOwnedPorts()) {
			if (port.getName().equals(selector.getBinding().getName())) {
				return port;
			}
		}
		return null;
	}

	private void addEndToLinkAction(ReadLinkAction rla, Port port, Expr base, int i) {
		LinkEndData end = rla.createEndData();
		InputPin endPin = (InputPin) rla.createInputValue(rla.getName() + "_end" + i + "input",
				base.getType(), UMLPackage.Literals.INPUT_PIN);
		end.setValue(endPin);
		end.setEnd(port);
		expressionExporter.createObjectFlowBetweenActivityNodes(base.getOutputPin(), end.getValue());
	}

}
