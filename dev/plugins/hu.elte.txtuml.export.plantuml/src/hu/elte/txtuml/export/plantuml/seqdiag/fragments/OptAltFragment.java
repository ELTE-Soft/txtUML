package hu.elte.txtuml.export.plantuml.seqdiag.fragments;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlPreCompiler;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

public class OptAltFragment extends CombinedFragmentExporter<IfStatement> {

	protected PlantUmlPreCompiler walker;

	public OptAltFragment(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode node) {
		boolean validElement = super.validElement(node);
		boolean isIfStatement = node.getNodeType() == ASTNode.IF_STATEMENT;

		return validElement && isIfStatement;
	}

	@Override
	public boolean preNext(IfStatement curElement) {

		Statement thenStatement = curElement.getThenStatement();
		Statement elseStatement = curElement.getElseStatement();
		Expression condition = curElement.getExpression();

		if (elseStatement == null) {
			compiler.println("opt " + condition.toString());
			return true;
		} else {
			compiler.println("alt " + condition.toString());
			thenStatement.accept(compiler);
			if (elseStatement instanceof IfStatement) {
				processAltStatement((IfStatement) elseStatement);
			} else {
				compiler.println("else");
				elseStatement.accept(compiler);
			}
			return false;
		}
	}

	private void processAltStatement(IfStatement statement) {
		Statement thenStatement = statement.getThenStatement();
		Statement elseStatement = statement.getElseStatement();
		Expression condition = statement.getExpression();

		compiler.println("else " + condition.toString());
		thenStatement.accept(compiler);
		if (elseStatement != null) {
			if (elseStatement instanceof IfStatement) {
				processAltStatement((IfStatement) elseStatement);
			} else {
				compiler.println("else");
				elseStatement.accept(compiler);
			}
		}
	}
}
