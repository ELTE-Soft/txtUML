package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StructuredActivityNode;

import hu.elte.txtuml.export.uml2.transform.backend.ParameterMap;
import hu.elte.txtuml.export.uml2.transform.backend.VariableMap;
import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.TypeExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public abstract class AbstractControlStructureExporter {

	protected final BlockExporter<? extends ActivityNode> blockExporter;
	private final ParameterMap params;
	private final VariableMap vars;
	private final TypeExporter typeExporter;

	public AbstractControlStructureExporter(BlockExporter<? extends ActivityNode> blockExporter) {
		this.blockExporter = blockExporter;
		this.params = blockExporter.getParameters();
		this.vars = blockExporter.getVariables();
		this.typeExporter = blockExporter.getTypeExporter();
	}

	protected <NodeType extends ActivityNode> BlockExporter<NodeType> createBlockExporter(
			StructuredActivityNode controlStructure, EList<NodeType> nodeList) {

		return new BlockExporter<NodeType>(controlStructure, nodeList, params, vars, typeExporter,
				blockExporter.getMethodBodyExporter());
	}

	protected ExpressionExporter<ExecutableNode> createExpressionExporter(StructuredActivityNode controlStructure,
			EList<ExecutableNode> nodeList) {

		return new ExpressionExporter<ExecutableNode>(controlStructure, nodeList, params, vars, typeExporter);
	}

	protected ExpressionExporter<ExecutableNode> createExpressionExporter(SequenceNode sequenceNode) {

		return new ExpressionExporter<ExecutableNode>(sequenceNode, sequenceNode.getExecutableNodes(), params, vars,
				typeExporter);
	}

}
