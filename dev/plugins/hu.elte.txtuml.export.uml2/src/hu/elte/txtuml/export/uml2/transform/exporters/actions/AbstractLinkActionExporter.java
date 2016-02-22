package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public abstract class AbstractLinkActionExporter {
	protected final ExpressionExporter<? extends ActivityNode> expressionExporter;

	protected AbstractLinkActionExporter(ExpressionExporter<? extends ActivityNode> expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public abstract void export(IMethodBinding binding, List<Expr> args);

	protected void export(IMethodBinding binding, List<Expr> args, EClass actionEClass) {
		args.forEach(Expr::evaluate);

		Association association = getAssociation(binding, args);

		Expr leftExpr = args.get(1);
		String leftName = leftExpr.getName();
		Expr rightExpr = args.get(3);
		String rightName = rightExpr.getName();

		String prefix = UMLPackage.Literals.CREATE_LINK_ACTION.equals(actionEClass) ? "link_" : "unlink";
		String linkActionName = prefix + leftName + "_and_" + rightName;

		LinkAction linkAction = (LinkAction) expressionExporter.createAndAddNode(linkActionName, actionEClass);

		this.addEndToLinkAction(linkAction, association, leftExpr, 1);
		this.addEndToLinkAction(linkAction, association, rightExpr, 2);
	}

	protected void addEndToLinkAction(LinkAction linkAction, Association association, Expr endExpr, int endNum) {

		String phrase = endExpr.getName();
		Type endType = endExpr.getType();

		LinkEndData end = linkAction.createEndData();
		Property endProp = association.getMemberEnd(phrase, endType);

		InputPin endPin = (InputPin) linkAction.createInputValue(linkAction.getName() + "_end" + endNum + "input",
				endType, UMLPackage.Literals.INPUT_PIN);

		ObjectNode endExprObjNode = endExpr.getObjectNode();

		end.setEnd(endProp);
		end.setValue(endPin);

		expressionExporter.createObjectFlowBetweenActivityNodes(endExprObjNode, endPin);
	}

	protected Association getAssociation(IMethodBinding binding, List<Expr> args) {
		TypeLiteral arg0 = (TypeLiteral) args.get(0);

		ITypeBinding assocTypeBinding = arg0.getType().resolveBinding().getDeclaringClass();

		return (Association) expressionExporter.getTypeExporter().exportType(assocTypeBinding);
	}

}
