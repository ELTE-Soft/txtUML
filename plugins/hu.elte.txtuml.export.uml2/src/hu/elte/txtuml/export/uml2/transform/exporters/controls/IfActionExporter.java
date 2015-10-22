package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.AssignableExpr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.OperatorExporter;
import hu.elte.txtuml.utils.Reference;

import java.util.Arrays;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

public class IfActionExporter extends AbstractControlStructureExporter {

	private ConditionalNode conditionalNode;
	private Expr condition;
	private Clause thenClause;
	private Clause elseClause;
	private ExpressionExporter testsExporter;

	public IfActionExporter(BlockExporter blockExporter) {
		super(blockExporter);
	}

	public void exportIfStatement(IfStatement statement) {
		conditionalNode = (ConditionalNode) blockExporter.createExecutableNode(
				null, UMLPackage.Literals.CONDITIONAL_NODE);

		thenClause = UMLFactory.eINSTANCE.createClause();
		SequenceNode conditionSequence = (SequenceNode) conditionalNode.createNode("cond",
				UMLPackage.Literals.SEQUENCE_NODE);
		thenClause.getTests().add(conditionSequence);
		testsExporter = createExpressionExporter(conditionSequence,
				conditionSequence.getExecutableNodes());

		condition = testsExporter.export(statement.getExpression());

		conditionalNode.setName("if " + condition.getName());

		if (statement.getElseStatement() != null) {
			createElse();
		} else {
			thenClause.setDecider(condition.evaluate().getOutputPin());
		}

		exportClause(thenClause, "then", statement.getThenStatement());

		if (elseClause != null) {
			exportClause(elseClause, "else", statement.getElseStatement());
		}
	}

	private void createElse() {
		elseClause = UMLFactory.eINSTANCE.createClause();

		OperatorExporter operatorExporter = new OperatorExporter(testsExporter);

		Reference<Expr> thenExpr = Reference.empty();
		Reference<Expr> elseExpr = Reference.empty();
		operatorExporter.export("fork", Arrays.asList(condition,
				createDummyExprToGetOutput("then", thenExpr),
				createDummyExprToGetOutput("else", elseExpr)));

		operatorExporter = new OperatorExporter(createExpressionExporter(
				conditionalNode, elseClause.getTests()));

		Expr output = operatorExporter.export("!_",
				Arrays.asList(elseExpr.get())).getSecond();

		/*
		 * Calling 'evaluate' is unnecessary as in this case, the Expr instances
		 * are guaranteed to have been created from an output pin and so
		 * 'evaluate' would do nothing.
		 */
		thenClause.setDecider(thenExpr.get().getOutputPin());
		elseClause.setDecider(output.getOutputPin());
	}

	private void exportClause(Clause clause, String nameOfClause,
			Statement statementToExport) {
		conditionalNode.getClauses().add(clause);

		BlockExporter clauseExporter = createBlockExporter(conditionalNode,
				clause.getBodies());

		clause.getBodies().add(
				BlockExporter.exportBlock(clauseExporter, statementToExport,
						nameOfClause));
	}

	private Expr createDummyExprToGetOutput(String name, Reference<Expr> output) {
		return new AssignableExpr() {

			@Override
			public Type getType() {
				return blockExporter.getTypeExporter().getBoolean();
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public OutputPin getOutputPin() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void setValue(Expr newValue) {
				output.set(newValue);
			}

		};

	}

}
