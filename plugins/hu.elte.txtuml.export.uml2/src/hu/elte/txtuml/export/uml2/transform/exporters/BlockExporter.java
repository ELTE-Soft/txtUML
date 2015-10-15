package hu.elte.txtuml.export.uml2.transform.exporters;

import hu.elte.txtuml.export.uml2.transform.backend.ParameterMap;
import hu.elte.txtuml.export.uml2.transform.backend.VariableMap;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;
import hu.elte.txtuml.export.uml2.transform.visitors.BlockVisitor;
import hu.elte.txtuml.export.uml2.utils.ControlStructureEditor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StructuredActivityNode;

public class BlockExporter extends ControlStructureEditor {

	private final ParameterMap params;
	private final VariableMap vars;
	private final TypeExporter typeExporter;
	private final ExpressionExporter expressionExporter;

	/**
	 * This constructor is public only to allow customization for special
	 * exports. Usage of {@link #exportBody} or {@link #exportBlock} is advised
	 * if possible.
	 * 
	 * @param controlStructure
	 *            the control structure to add the edges and variables to
	 * @param nodeList
	 *            the list to add the exported nodes to
	 * @param params
	 *            parameters in scope
	 * @param vars
	 *            variables in scope
	 * @param typeExporter
	 *            a type exporter
	 */
	public BlockExporter(StructuredActivityNode controlStructure,
			EList<ExecutableNode> nodeList, ParameterMap params,
			VariableMap vars, TypeExporter typeExporter) {
		super(controlStructure, nodeList);

		this.params = params;
		this.vars = vars;
		this.typeExporter = typeExporter;
		this.expressionExporter = new ExpressionExporter(this);
	}

	/**
	 * Exports main block of method.
	 */
	public static SequenceNode exportBody(
			MethodBodyExporter exporterOfContainerMethod, Block block) {

		SequenceNode sequenceNode = exporterOfContainerMethod
				.createSequenceNode("body");

		ParameterMap params = exporterOfContainerMethod.getParameters();
		VariableMap vars = VariableMap.create();
		TypeExporter typeExporter = exporterOfContainerMethod.getTypeExporter();

		new BlockExporter(sequenceNode, sequenceNode.getExecutableNodes(),
				params, vars, typeExporter).export(block);

		return sequenceNode;
	}

	/**
	 * Exports inner blocks.
	 */
	public static SequenceNode exportBlock(BlockExporter exporterOfParentBlock,
			Statement block, String nameOfBlock) {
		SequenceNode sequenceNode = exporterOfParentBlock
				.createSequenceNode(nameOfBlock);

		ParameterMap params = exporterOfParentBlock.getParameters();
		VariableMap vars = exporterOfParentBlock.getVariables().copy();
		TypeExporter typeExporter = exporterOfParentBlock.getTypeExporter();

		new BlockExporter(sequenceNode, sequenceNode.getExecutableNodes(),
				params, vars, typeExporter).export(block);

		return sequenceNode;
	}

	@SuppressWarnings("unchecked")
	public void export(Statement block) {
		BlockVisitor visitor = new BlockVisitor(this);
		if (block instanceof Block) {
			((Block) block).statements().forEach(
					o -> ((ASTNode) o).accept(visitor));
		} else {
			block.accept(visitor);
		}
	}
	
	public TypeExporter getTypeExporter() {
		return typeExporter;
	}

	public ExpressionExporter getExpressionExporter() {
		return expressionExporter;
	}

	public ParameterMap getParameters() {
		return params;
	}

	public VariableMap getVariables() {
		return vars;
	}

}
