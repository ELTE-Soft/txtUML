package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.uml2.uml.ActivityNode;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;

public class WhileActionExporter extends AbstractLoopExporter {

	public WhileActionExporter(BlockExporter<? extends ActivityNode> blockExporter) {
		super(blockExporter);
	}

	public void exportWhileStatement(WhileStatement statement) {

		exportLoop("while", null, statement.getExpression(), null, statement.getBody());

	}
}
