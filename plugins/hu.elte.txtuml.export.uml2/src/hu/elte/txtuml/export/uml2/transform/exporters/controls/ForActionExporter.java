package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.uml2.uml.ActivityNode;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;

public class ForActionExporter extends AbstractLoopExporter {

	public ForActionExporter(BlockExporter<? extends ActivityNode> blockExporter) {
		super(blockExporter);
	}

	@SuppressWarnings("unchecked")
	public void exportForStatement(ForStatement statement) {

		exportLoop("for", statement.initializers(), statement.getExpression(), statement.updaters(),
				statement.getBody());

	}

}
