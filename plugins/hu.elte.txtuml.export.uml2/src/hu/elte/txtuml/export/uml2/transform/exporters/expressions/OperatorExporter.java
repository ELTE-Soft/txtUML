package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import hu.elte.txtuml.export.uml2.transform.exporters.TypeExporter;
import hu.elte.txtuml.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Type;

public class OperatorExporter {

	private final ExpressionExporter expressionExporter;

	public OperatorExporter(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
	}

	public Pair<Operation, Expr> exportRaw(String operator,
			List<Expression> arguments) {
		List<Expr> args = new ArrayList<>();
		arguments.forEach(a -> args.add(expressionExporter.export(a)));
		return export(operator, args);
	}

	public Pair<Operation, Expr> export(String operator, List<Expr> arguments) {
		TypeExporter typeExporter = expressionExporter.getTypeExporter();
		Type type = arguments.get(0).getType();

		Operation operation = getOperation(typeExporter, type, operator);

		Expr ret = expressionExporter.createCallOperationAction(operation,
				null, arguments);

		return Pair.create(operation, ret);
	}

	private Operation getOperation(TypeExporter typeExporter, Type type,
			String operator) {
		if (typeExporter.isInteger(type)) {
			return typeExporter.getIntegerOperations().getOperation(
					getIntegerOperationName(operator), null, null);
		}

		if (typeExporter.isBoolean(type)) {
			return typeExporter.getBooleanOperations().getOperation(
					getBooleanOperationName(operator), null, null);
		}

		if (typeExporter.isString(type)) {
			return typeExporter.getStringOperations().getOperation(
					getStringOperationName(operator), null, null);
		}

		return typeExporter.getObjectOperations().getOperation(
				getObjectOperationName(operator), null, null);
	}

	private String getIntegerOperationName(String operator) {
		switch (operator) {
		case "+":
			return "add";
		case "-":
			return "sub";
		case "*":
			return "mul";
		case "/":
			return "div";
		case "%":
			return "mod";
		case "++_":
			return "inc";
		case "--_":
			return "dec";
		case "+_":
			return "id";
		case "-_":
			return "neg";
		case "_++":
			return "delayedInc";
		case "_--":
			return "delayedDec";
		case "<":
			return "lt";
		case "<=":
			return "leq";
		case ">":
			return "gt";
		case ">=":
			return "geq";
		case "==":
			return "eq";
		case "!=":
			return "neq";
		}
		return operator;
	}

	private String getBooleanOperationName(String operator) {
		switch (operator) {
		case "&&":
			return "and";
		case "||":
			return "or";
		case "!_":
			return "not";
		case "==":
			return "eq";
		case "!=":
			return "neq";
		}
		return operator;
	}

	private String getStringOperationName(String operator) {
		switch (operator) {
		case "+":
			return "concat";
		}
		return operator;
	}

	private String getObjectOperationName(String operator) {
		switch (operator) {
		case "==":
			return "eq";
		case "!=":
			return "neq";
		}
		return operator;
	}

}
