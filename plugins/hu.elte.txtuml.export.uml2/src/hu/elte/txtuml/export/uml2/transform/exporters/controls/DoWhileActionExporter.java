package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;

public class DoWhileActionExporter extends AbstractLoopExporter {

	public DoWhileActionExporter(BlockExporter<? extends ActivityNode> blockExporter) {
		super(blockExporter);
	}

	public void exportDoWhileStatement(DoStatement node) {
		String name = "dowhile";
		
		LoopNode loopNode = (LoopNode) blockExporter.createAndAddNode(name ,
				UMLPackage.Literals.LOOP_NODE);

		exportBlock(loopNode, loopNode.getSetupParts(), node.getBody(), "init");

		Expr exportedCondition = exportCondition(loopNode, loopNode.getTests(),
				node.getExpression(), "cond");
		loopNode.setName(name + " "  + exportedCondition.getName());
		loopNode.setDecider(exportedCondition.evaluate().getOutputPin());

		exportBlock(loopNode, loopNode.getBodyParts(), node.getBody(), "body");
	}

}
