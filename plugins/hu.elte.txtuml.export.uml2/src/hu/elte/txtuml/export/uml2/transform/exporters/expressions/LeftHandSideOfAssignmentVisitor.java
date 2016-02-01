package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import java.util.Arrays;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.uml2.uml.Type;

class LeftHandSideOfAssignmentVisitor extends ASTVisitor {

	private final Expr rightHandSide;
	private final ExpressionExporter expressionExporter;
	private String name;
	private Operator operator;
	private OperatorExporter operatorExporter;

	public LeftHandSideOfAssignmentVisitor(Expr result, ExpressionExporter expressionExporter,
			OperatorExporter operatorExporter, Operator operator) {
		this.rightHandSide = result;
		this.expressionExporter = expressionExporter;
		this.operatorExporter = operatorExporter;
		this.operator = operator;
	}

	@Override
	public boolean visit(FieldAccess node) {
		Expr target = expressionExporter.export(node.getExpression());

		applyOperatorOn(Expr.field(target, node.resolveFieldBinding(), expressionExporter));
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		IVariableBinding binding = (IVariableBinding) node.resolveBinding();
		// As the left hand side of an assignment, it must be a variable.

		applyOperatorOn(Expr.ofName(binding, node.getFullyQualifiedName(), expressionExporter));

		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		IVariableBinding binding = (IVariableBinding) node.resolveBinding();
		// As the left hand side of an assignment, it must be a variable.

		applyOperatorOn(Expr.ofName(binding, expressionExporter));

		return false;
	}

	private void applyOperatorOn(Expr expr) {
		if (operator.equals(Operator.ASSIGN)) {
			expr.setValue(rightHandSide);
		} else {
			// compound operators
			String infixOpStr = getNormalOperatorForCompound(operator, expr.getType());
			expr.setValue(operatorExporter.export(infixOpStr, Arrays.asList(expr, rightHandSide)).getSecond());
		}
	}

	private String getNormalOperatorForCompound(Operator operator, Type rhsType) {
		switch (operator.toString()) {
		case "+=":
			return "+";
		case "-=":
			return "-";
		case "*=":
			return "*";
		case "/=":
			return "/";
		case "%=":
			return "%";
		case "&=":
			return "&&";
		case "|=":
			return "||";
		default:
			throw new RuntimeException("Unrecognized compound operator: " + operator);
		}
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		// TODO SuperFieldAccess
		return false;
	}

	public String getName() {
		return name;
	}

}
