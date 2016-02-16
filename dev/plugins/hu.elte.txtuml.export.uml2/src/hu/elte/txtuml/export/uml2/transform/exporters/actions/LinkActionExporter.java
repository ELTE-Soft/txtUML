package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import java.util.List;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public class LinkActionExporter {

	@SuppressWarnings("unused")
	private final ExpressionExporter expressionExporter;

	public LinkActionExporter(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
	}
	
	public void export(List<Expr> args) {
		// TODO export link
	}

	/*
	private String obtainAssocName(MethodInvocation methodInvocation) {
		Expression argExpr = (Expression) methodInvocation.arguments().get(0);
		ITypeBinding type = argExpr.resolveTypeBinding();

		ITypeBinding declaringClass = type.getTypeArguments()[0]
				.getDeclaringClass();

		return declaringClass.getName();
	}

	@Override
	public void exportFromMethodInvocation(IMethodBinding binding) {
		String assocName = this.obtainAssocName(binding);
		Association association = (Association) this.exportedModel
				.getOwnedMember(assocName);

		if (binding.getName().toString()
				.equals(LinkActionExporter.ACTION_LINK_NAME)) {
			exportLinkAction(binding, association, true);
		} else {
			exportLinkAction(binding, association, false);
		}
	}

	private void exportLinkAction(MethodInvocation methodInvocation,
			Association association, Boolean link) {
		String leftName = this.obtainExpressionOfNthArgument(methodInvocation,
				1);
		String rightName = this.obtainExpressionOfNthArgument(methodInvocation,
				3);
		Type rightType = this.obtainTypeOfNthArgument(methodInvocation, 2);
		Type leftType = this.obtainTypeOfNthArgument(methodInvocation, 4);
		String leftPhrase = leftType.getName();
		String rightPhrase = rightType.getName();

		String linkActionName = "link_" + leftName + "_and_" + rightName;

		LinkAction linkAction;

		if (link) {
			linkAction = (LinkAction) this.activity.createOwnedNode(
					linkActionName, UMLPackage.Literals.CREATE_LINK_ACTION);
		} else {
			linkActionName = "un" + linkActionName;
			linkAction = (LinkAction) this.activity.createOwnedNode(
					linkActionName, UMLPackage.Literals.DESTROY_LINK_ACTION);
		}

		this.addEndToLinkAction(linkAction, association, leftName, leftPhrase,
				leftType, 1);
		this.addEndToLinkAction(linkAction, association, rightName,
				rightPhrase, rightType, 2);

		this.methodBodyExporter.getBodyNode().getExecutableNodes()
				.add(linkAction);

	}

	private void addEndToLinkAction(LinkAction linkAction,
			Association association, String phrase, String instName,
			Type endType, int endNum) {

		ValuePin endValuePin = (ValuePin) linkAction.createInputValue(
				linkAction.getName() + "_end" + endNum + "input", endType,
				UMLPackage.Literals.VALUE_PIN);

		this.createAndAddOpaqueExpressionToValuePin(endValuePin, instName,
				endType);

		LinkEndData end = linkAction.createEndData();
		Property endProp = association.getMemberEnd(phrase, endType);

		end.setEnd(endProp);
		end.setValue(endValuePin);
	}
	*/
}
