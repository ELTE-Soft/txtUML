package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.List;

import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.UMLPackage;

public class StartActionExporter {

	private final ExpressionExporter expressionExporter;

	public StartActionExporter(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public void export(List<Expr> args) {
		Expr arg = args.get(0);

		StartClassifierBehaviorAction startAction = (StartClassifierBehaviorAction) expressionExporter
				.createExecutableNode("start " + arg.getName(),
						UMLPackage.Literals.START_CLASSIFIER_BEHAVIOR_ACTION);

		startAction.createObject(arg.getName(), arg.getType());

	}
}
