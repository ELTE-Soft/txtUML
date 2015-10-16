package hu.elte.txtuml.export.uml2.transform.exporters.actions;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.Iterator;
import java.util.List;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLPackage;

public class CreateObjectActionExporter {

	private final ExpressionExporter expressionExporter;

	public CreateObjectActionExporter(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public Expr export(List<Expr> args) {
		args.forEach(Expr::evaluate);

		Iterator<Expr> it = args.iterator();
		Expr type = it.next();
		it.remove();
		CreateObjectAction createAction = (CreateObjectAction) expressionExporter
				.createExecutableNode("instantiate " + type.getName(),
						UMLPackage.Literals.CREATE_OBJECT_ACTION);
		Expr target = Expr.ofPin(
				createAction.createResult("new " + type.getName(),
						type.getType()), "new " + type.getName());

		Operation constructor = expressionExporter.getTypeExporter()
				.exportMethodAsOperation((Classifier) type.getType(),
						type.getName(), type.getType(), args);
		
		if (constructor == null) { // in case of default constructor
			return target;
		} else {
			return expressionExporter.createCallOperationAction(constructor,
					target, args);			
		}

	}
}
