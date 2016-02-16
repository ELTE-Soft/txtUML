package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;

class LeftHandSideOfAssignmentVisitor extends ASTVisitor {

	private final Expr rightHandSide;
	private final ExpressionExporter expressionExporter;
	private String name;

	public LeftHandSideOfAssignmentVisitor(Expr result,
			ExpressionExporter expressionExporter) {
		this.rightHandSide = result;
		this.expressionExporter = expressionExporter;
	}

	@Override
	public boolean visit(FieldAccess node) {
		Expr target = expressionExporter.export(node.getExpression());

		Expr.field(target, node.resolveFieldBinding(), expressionExporter)
				.setValue(rightHandSide);
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		IVariableBinding binding = (IVariableBinding) node.resolveBinding();
		// As the left hand side of an assignment, it must be a variable.

		Expr.ofName(binding, expressionExporter).setValue(rightHandSide);

		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		IVariableBinding binding = (IVariableBinding) node.resolveBinding();
		// As the left hand side of an assignment, it must be a variable.

		Expr.ofName(binding, expressionExporter).setValue(rightHandSide);

		return false;
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
