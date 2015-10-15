package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.List;

import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.UMLPackage;

public class DeleteObjectActionExporter {

	private final ExpressionExporter expressionExporter;

	public DeleteObjectActionExporter(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public void export(List<Expr> args) {
		Expr arg = args.get(0);
		
		DestroyObjectAction deleteAction = (DestroyObjectAction) expressionExporter
				.createExecutableNode("delete " + arg.getName(),
						UMLPackage.Literals.DESTROY_OBJECT_ACTION);
		
		deleteAction.createTarget(arg.getName(), arg.getType());
	}
}
