package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.exporters.actions.AbstractLinkActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.TypeLiteralExpr;

public class NavigationActionExporter extends AbstractLinkActionExporter {

	protected NavigationActionExporter(ExpressionExporter<? extends ActivityNode> expressionExporter) {
		super(expressionExporter);
	}

	public Expr export(IMethodBinding binding, Expr base, List<Expr> args) {
		TypeLiteralExpr selectorExpr = (TypeLiteralExpr) args.get(0);
		ITypeBinding[] selectors = selectorExpr.getBinding().getDeclaringClass().getDeclaredTypes();
		ITypeBinding otherSelector = Stream.of(selectors).filter(sel -> sel != selectorExpr.getBinding()).findAny()
				.get();
		ITypeBinding selectorType = selectorExpr.getBinding().getSuperclass().getTypeArguments()[0];

		Association assoc = getAssociation(binding, args);

		String nodeName = base.getName() + "->" + selectorExpr.getName();
		ReadLinkAction rla = (ReadLinkAction) expressionExporter.createAndAddNode(nodeName,
				UMLPackage.Literals.READ_LINK_ACTION);
		OutputPin res = rla.createResult("res" + rla.getName(),
				expressionExporter.getTypeExporter().exportType(selectorType));

		addEndToLinkAction(rla, assoc, otherSelector.getName(), base.evaluate(), 1);
		return Expr.ofPin(res, res.getName());
	}

}
