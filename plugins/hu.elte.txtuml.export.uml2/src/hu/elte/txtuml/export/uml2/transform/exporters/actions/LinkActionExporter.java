package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.UMLPackage;

public class LinkActionExporter extends AbstractLinkActionExporter {

	public LinkActionExporter(ExpressionExporter expressionExporter) {
		super(expressionExporter);
	}
	
	@Override
	public void export(MethodInvocation methodInvocation, List<Expr> args) {
		export(methodInvocation, args, UMLPackage.Literals.CREATE_LINK_ACTION);	
	}

}
