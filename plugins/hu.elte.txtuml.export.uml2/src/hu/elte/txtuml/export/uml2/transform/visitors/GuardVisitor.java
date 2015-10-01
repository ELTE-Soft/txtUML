package hu.elte.txtuml.export.uml2.transform.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;

public class GuardVisitor extends ASTVisitor {
	private String guardExpression;
	
	public GuardVisitor() {
		this.guardExpression = "";
	}
	@Override
	public boolean visit(ReturnStatement returnStatement) {
		Expression returnExpression = returnStatement.getExpression();
		ExpressionVisitor visitor = new ExpressionVisitor();
		returnExpression.accept(visitor);
		this.guardExpression = visitor.getImportedExpression();
		return false;
	}
	
	public String getGuardExpression() {
		return this.guardExpression;
	}
}
