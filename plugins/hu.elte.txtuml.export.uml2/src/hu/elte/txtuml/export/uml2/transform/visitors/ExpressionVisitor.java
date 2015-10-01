package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.utils.OperatorConverter;

import java.util.LinkedList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;

public class ExpressionVisitor extends ASTVisitor {
	private String importedExpression;
	
	public String getImportedExpression() {
		return this.importedExpression;
	}
	public ExpressionVisitor() {
		this.importedExpression = "";
	}
	
	@Override
	public boolean visit(PostfixExpression expression) {
		ExpressionVisitor operandVisitor = new ExpressionVisitor();
		expression.getOperand().accept(operandVisitor);
		String operator = OperatorConverter.convert(expression.getOperator().toString());
		this.importedExpression = operandVisitor.getImportedExpression() + operator;
		return false;
	}
	
	@Override
	public boolean visit(ThisExpression expression) {
		this.importedExpression = "self";
		return false;
	}
	
	@Override
	public boolean visit(PrefixExpression expression) {
		ExpressionVisitor operandVisitor = new ExpressionVisitor();
		expression.getOperand().accept(operandVisitor);
		String operator = OperatorConverter.convert(expression.getOperator().toString());
		this.importedExpression =  operator + operandVisitor.getImportedExpression();
		return false;
	}
	
	@Override
	public boolean visit(InfixExpression expression) {
		ExpressionVisitor leftVisitor = new ExpressionVisitor();
		ExpressionVisitor rightVisitor = new ExpressionVisitor();
		Expression left = expression.getLeftOperand();
		Expression right = expression.getRightOperand();
		left.accept(leftVisitor);
		right.accept(rightVisitor);
		String operator = OperatorConverter.convert(expression.getOperator().toString());
		this.importedExpression = leftVisitor.getImportedExpression() + " " +
				operator + " " +
				rightVisitor.getImportedExpression();
		
		for(Object o : expression.extendedOperands()) {
			Expression operand = (Expression)o;
			ExpressionVisitor extendedOperandVisitor = new ExpressionVisitor();
			operand.accept(extendedOperandVisitor);
			operator = OperatorConverter.convert(expression.getOperator().toString());
			this.importedExpression += " " + operator + " " + extendedOperandVisitor.getImportedExpression();
		}
		return false;
	}
	
	@Override
	public boolean visit(ParenthesizedExpression parenthesizedExpression) {
		Expression expression = parenthesizedExpression.getExpression();
		ExpressionVisitor visitor = new ExpressionVisitor();
		expression.accept(visitor);
		this.importedExpression = "(" + visitor.getImportedExpression() + ")";
		return false;
	}
	
	@Override
	public boolean visit(MethodInvocation methodInvocation) {
		Expression expression = methodInvocation.getExpression();
		
		String leftHandExpr = "";
		if(expression != null) {
			ExpressionVisitor visitor = new ExpressionVisitor();
			expression.accept(visitor);
			leftHandExpr = visitor.getImportedExpression() + ".";
		}
		
		LinkedList<String> paramExpressions = new LinkedList<>();
		
		for (Object obj : methodInvocation.arguments()) {
			ExpressionVisitor argVisitor = new ExpressionVisitor();
			((Expression) obj).accept(argVisitor);
			paramExpressions.add(argVisitor.getImportedExpression());
		}
		
		String params = String.join(", ", paramExpressions);
		this.importedExpression = leftHandExpr + methodInvocation.getName().toString() + "(" + params + ")";
		return false;
	}
	
	@Override
	public boolean visit(FieldAccess fieldAccess) {
		ExpressionVisitor expressionVisitor = new ExpressionVisitor();
		Expression expression = fieldAccess.getExpression();
		expression.accept(expressionVisitor);
		this.importedExpression = expressionVisitor.getImportedExpression() + "." + fieldAccess.getName();
		return false;
	}
	
	@Override
	public boolean visit(SimpleName name) {
		this.importedExpression = name.toString();
		return true;
	}
	
	@Override
	public boolean visit(QualifiedName name) {
		this.importedExpression = name.getFullyQualifiedName();
		return true;
	}

	@Override
	public boolean visit(NumberLiteral literal) {
		this.importedExpression = literal.toString();
		return true;
	}

	@Override
	public boolean visit(StringLiteral literal) {
		this.importedExpression = literal.toString();
		return true;
	}

	@Override
	public boolean visit(BooleanLiteral literal) {
		this.importedExpression = literal.toString();
		return true;
	}

	@Override
	public boolean visit(CharacterLiteral literal) {
		this.importedExpression = literal.toString();
		return true;
	}

	@Override
	public boolean visit(NullLiteral literal) {
		this.importedExpression = literal.toString();
		return true;
	}

	@Override
	public boolean visit(TypeLiteral literal) {
		this.importedExpression = literal.toString();
		return true;
	}
	/*
	public boolean visit(SingleVariableDeclaration dec)
	{
		System.out.println("Variable Name:" + dec.getName());
		System.out.println("Variale type:" + dec.getType());
		return false;
	}*/
}
