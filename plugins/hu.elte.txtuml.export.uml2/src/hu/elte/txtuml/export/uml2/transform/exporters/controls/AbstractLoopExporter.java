package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.UMLPackage;

abstract class AbstractLoopExporter extends AbstractControlStructureExporter {

	AbstractLoopExporter(BlockExporter blockExporter) {
		super(blockExporter);
	}

	protected void exportLoop(String name, List<Expression> initializers,
			Expression condition, List<Expression> updaters, Statement body) {

		LoopNode loopNode = (LoopNode) blockExporter.createExecutableNode(name,
				UMLPackage.Literals.LOOP_NODE);

		exportList(loopNode, loopNode.getSetupParts(), initializers, "init");

		Expr exportedCondition = exportCondition(loopNode, loopNode.getTests(),
				condition, "cond");
		loopNode.setName(name + " "  + exportedCondition.getName());
		loopNode.setDecider(exportedCondition.evaluate().getOutputPin());

		exportBlock(loopNode, loopNode.getBodyParts(), body, "body");

		exportList(loopNode, loopNode.getBodyParts(), updaters, "update");

	}

	protected void exportList(LoopNode loopNode, EList<ExecutableNode> nodeList,
			List<Expression> list, String byName) {
		if (list == null || list.isEmpty()) {
			return;
		}
		SequenceNode sequenceNode = (SequenceNode) loopNode.createNode(byName,
				UMLPackage.Literals.SEQUENCE_NODE);
		nodeList.add(sequenceNode);
		sequenceNode.setName(byName);
		ExpressionExporter exporter = createExpressionExporter(loopNode,
				sequenceNode.getExecutableNodes());
		list.forEach(exporter::export);
	}

	protected Expr exportCondition(LoopNode loopNode,
			EList<ExecutableNode> nodeList, Expression expression, String byName) {
		SequenceNode sequenceNode = (SequenceNode) loopNode.createNode(byName,
				UMLPackage.Literals.SEQUENCE_NODE);
		nodeList.add(sequenceNode);
		sequenceNode.setName(byName);
		ExpressionExporter exporter = createExpressionExporter(loopNode,
				sequenceNode.getExecutableNodes());

		if (expression == null) {
			return Expr.trueExpression(exporter);
		} else {
			return exporter.export(expression);
		}
	}

	protected void exportBlock(LoopNode loopNode, EList<ExecutableNode> nodeList,
			Statement block, String byName) {
		if (block == null) {
			return;
		}
		SequenceNode sequenceNode = (SequenceNode) loopNode.createNode(byName,
				UMLPackage.Literals.SEQUENCE_NODE);
		nodeList.add(sequenceNode);
		sequenceNode.setName(byName);
		BlockExporter exporter = createBlockExporter(sequenceNode,
				sequenceNode.getExecutableNodes());
		exporter.export(block);
	}
}
