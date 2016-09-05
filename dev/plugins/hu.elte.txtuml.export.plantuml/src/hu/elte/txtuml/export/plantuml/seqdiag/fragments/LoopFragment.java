package hu.elte.txtuml.export.plantuml.seqdiag.fragments;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

public class LoopFragment extends CombinedFragmentExporter<Statement> {

	public LoopFragment(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {

		boolean validElement = super.validElement(curElement);
		boolean isLoopStatement = false;

		if (curElement instanceof WhileStatement || curElement instanceof ForStatement
				|| curElement.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
			isLoopStatement = true;
		}

		return validElement && isLoopStatement;
	}

	@Override
	public boolean preNext(Statement curElement) {
		switch (curElement.getNodeType()) {
		case ASTNode.WHILE_STATEMENT:
			exportWhile((WhileStatement) curElement);
			break;
		case ASTNode.FOR_STATEMENT:
			exportFor((ForStatement) curElement);
			break;
		case ASTNode.ENHANCED_FOR_STATEMENT:
			exportForEach((EnhancedForStatement) curElement);
			break;
		}

		return true;
	}

	protected void exportFor(ForStatement statement) {
		String initName = null;
		String initValue = null;
		Expression updater = null;
		Expression condition = statement.getExpression();

		if (statement.initializers().size() == 1) {
			VariableDeclarationExpression init = (VariableDeclarationExpression) statement.initializers().get(0);
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) init.fragments().get(0);
			initName = fragment.getName().toString();
			initValue = fragment.getInitializer().resolveConstantExpressionValue().toString();

		}

		if (statement.updaters().size() == 1) {
			updater = (Expression) statement.updaters().get(0);
		}

		compiler.println("loop from " + initName + "=" + initValue + " to " + condition.toString() + " by "
				+ updaterConverter(updater));
	}

	protected String updaterConverter(Expression updater) {
		if (updater.toString().equals("++i") || updater.toString().equals("i++")) {
			return "i+1";
		}

		return updater.toString();
	}

	protected void exportWhile(WhileStatement statement) {
		Expression condition = statement.getExpression();

		compiler.println("loop while " + condition.toString());
	}

	protected void exportForEach(EnhancedForStatement statement) {
		Expression loopVar = statement.getExpression();

		compiler.println("loop for each " + loopVar.toString());
	}

	@Override
	public boolean skippedStatement(ASTNode node) {
		return false;
	}

}
