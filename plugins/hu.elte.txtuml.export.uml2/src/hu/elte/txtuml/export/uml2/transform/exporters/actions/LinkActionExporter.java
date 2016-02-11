package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import java.util.List;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public class LinkActionExporter extends AbstractLinkActionExporter {

	public LinkActionExporter(ExpressionExporter<? extends ActivityNode> expressionExporter) {
		super(expressionExporter);
	}

	@Override
	public void export(IMethodBinding binding, List<Expr> args) {
		export(binding, args, UMLPackage.Literals.CREATE_LINK_ACTION);
	}

}
