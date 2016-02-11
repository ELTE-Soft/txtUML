package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ExpansionKind;
import org.eclipse.uml2.uml.ExpansionNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;

public class ForEachActionExporter {

	private BlockExporter<? extends ActivityNode> blockExporter;

	public ForEachActionExporter(BlockExporter<? extends ActivityNode> blockExporter) {
		this.blockExporter = blockExporter;
	}

	public void exportForEachStatement(EnhancedForStatement statement) {

		ExpansionRegion expRegion = (ExpansionRegion) blockExporter.createAndAddNode("foreach",
				UMLPackage.Literals.EXPANSION_REGION);
		expRegion.setMode(ExpansionKind.ITERATIVE_LITERAL);

		Expr expr = blockExporter.getExpressionExporter().export(statement.getExpression());

		BlockExporter<ActivityNode> subExporter = blockExporter.subExporter(expRegion, expRegion.getNodes());
		ExpansionNode inNode = (ExpansionNode) subExporter.createAndAddNode("foreach-in",
				UMLPackage.Literals.EXPANSION_NODE);
		expRegion.getInputElements().add(inNode);

		blockExporter.createObjectFlowBetweenActivityNodes(expr.getObjectNode(), inNode);

		Variable foreachVariable = subExporter.createVariable(statement.getParameter().getName().getIdentifier(),
				subExporter.getTypeExporter().exportType(statement.getParameter().resolveBinding().getType()));
		subExporter.getVariables().addVariable(foreachVariable);
		subExporter.export(statement.getBody());
	}

}
