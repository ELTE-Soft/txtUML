package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import java.util.List;

import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Operation;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public class LogActionExporter {

	private ExpressionExporter<? extends ActivityNode> expressionExporter;

	public LogActionExporter(ExpressionExporter<? extends ActivityNode> expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public void export(List<Expr> args) {
		args.get(0).evaluate();
		Operation operation = expressionExporter.getTypeExporter().getStringOperations().getOperation("log", null, null);
		expressionExporter.createCallOperationAction(operation, null, args);
	}

}
