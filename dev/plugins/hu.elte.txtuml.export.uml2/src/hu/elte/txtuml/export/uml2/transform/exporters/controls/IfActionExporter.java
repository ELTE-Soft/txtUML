package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import java.util.Arrays;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.OperatorExporter;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.AssignableExpr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.OperatorExporter;
import hu.elte.txtuml.utils.Reference;

public class IfActionExporter extends AbstractControlStructureExporter {

	private ConditionalNode conditionalNode;
	private Expr condition;
	private Clause thenClause;
	private Clause elseClause;
	private ExpressionExporter<? extends ActivityNode> testsExporter;

	public IfActionExporter(BlockExporter<? extends ActivityNode> blockExporter) {
		super(blockExporter);
	}

	public void exportIfStatement(IfStatement statement) {

		Variable var = blockExporter.createVariable("#if_cond_variable", blockExporter.getTypeExporter().getBoolean());

		conditionalNode = (ConditionalNode) blockExporter.createAndAddNode(null, UMLPackage.Literals.CONDITIONAL_NODE);

		thenClause = UMLFactory.eINSTANCE.createClause();
		SequenceNode conditionSequence = (SequenceNode) conditionalNode.createNode("cond",
				UMLPackage.Literals.SEQUENCE_NODE);
		thenClause.getTests().add(conditionSequence);
		testsExporter = createExpressionExporter(conditionSequence, conditionSequence.getExecutableNodes());

		condition = testsExporter.export(statement.getExpression());
		testsExporter.createWriteVariableAction(var, condition);

		conditionalNode.setName("if " + condition.getName());
		OutputPin thenExpr = testsExporter.createReadVariableAction(var);

		if (statement.getElseStatement() != null) {
			elseClause = UMLFactory.eINSTANCE.createClause();

			OperatorExporter operatorExporter = new OperatorExporter(
					createExpressionExporter(conditionalNode, elseClause.getTests()));

			Expr output = operatorExporter.export("!_", Arrays.asList(Expr.ofPin(thenExpr, "cond"))).getSecond();
			elseClause.setDecider(output.getOutputPin());
			exportClause(elseClause, "else", statement.getElseStatement());
		}

		exportClause(thenClause, "then", statement.getThenStatement());
		thenClause.setDecider(thenExpr);
	}

	private void exportClause(Clause clause, String nameOfClause, Statement statementToExport) {
		conditionalNode.getClauses().add(clause);

		BlockExporter<? extends ActivityNode> clauseExporter = createBlockExporter(conditionalNode, clause.getBodies());

		clause.getBodies().add(BlockExporter.exportBlock(clauseExporter, statementToExport, nameOfClause));
	}
}
