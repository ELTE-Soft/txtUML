package hu.elte.txtuml.seqdiag.export.plantuml.exporters.fragments;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for exporting OPT and ALT
 * combined fragments.
 */
public class OptAltFragmentExporter extends CombinedFragmentExporter<IfStatement> {

	public OptAltFragmentExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode node) {
		boolean validElement = super.validElement(node);
		return validElement && node.getNodeType() == ASTNode.IF_STATEMENT;
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
