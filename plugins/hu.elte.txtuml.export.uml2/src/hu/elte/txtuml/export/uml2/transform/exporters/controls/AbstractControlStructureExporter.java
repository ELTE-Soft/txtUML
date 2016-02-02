package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import hu.elte.txtuml.export.uml2.transform.backend.ParameterMap;
import hu.elte.txtuml.export.uml2.transform.backend.VariableMap;
import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.TypeExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StructuredActivityNode;

public abstract class AbstractControlStructureExporter {

	protected final BlockExporter blockExporter;
	private final ParameterMap params;
	private final VariableMap vars;
	private final TypeExporter typeExporter;

	public AbstractControlStructureExporter(BlockExporter blockExporter) {
		this.blockExporter = blockExporter;
		this.params = blockExporter.getParameters();
		this.vars = blockExporter.getVariables();
		this.typeExporter = blockExporter.getTypeExporter();
	}

	protected BlockExporter createBlockExporter(
			StructuredActivityNode controlStructure,
			EList<ExecutableNode> nodeList) {

		return new BlockExporter(controlStructure, nodeList, params, vars,
				typeExporter, blockExporter.getMethodBodyExporter());
	}

	protected ExpressionExporter createExpressionExporter(
			StructuredActivityNode controlStructure,
			EList<ExecutableNode> nodeList) {

		return new ExpressionExporter(controlStructure, nodeList, params, vars,
				typeExporter);
	}

	protected ExpressionExporter createExpressionExporter(
			SequenceNode sequenceNode) {

		return new ExpressionExporter(sequenceNode,
				sequenceNode.getExecutableNodes(), params, vars, typeExporter);
	}

}
