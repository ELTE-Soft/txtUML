package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;
import hu.elte.txtuml.export.plantuml.seqdiag.fragments.LoopFragmentExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.fragments.OptAltFragmentExporter;

/**
 * Abstract base class for sequence diagram exporter implementations. This class
 * also decides which node type is parsed by which exporter class. See
 * {@link #createExporter(ASTNode, PlantUmlCompiler)} method which creates the
 * appropriate exporter class implementation.
 *
 * @param <T>
 *            The node type which is parsed by the exporter.
 */
public abstract class ExporterBase<T extends ASTNode> {

	protected PlantUmlCompiler compiler;

	public ExporterBase(final PlantUmlCompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * Determines whether the given element can be processed by this exporter.
	 * 
	 * @param curElement
	 *            The current element to be parsed.
	 * @return True if the received node is parseable by this exporter, false
	 *         otherwise.
	 */
	public abstract boolean validElement(ASTNode curElement);

	/**
	 * Defines what to do when this node is visited for the first time.
	 * 
	 * @param curElement
	 *            The current element to be parsed.
	 * @return True if child nodes should be visited, false otherwise.
	 */
	public abstract boolean preNext(T curElement);

	/**
	 * Defines what to do at the end of the visit (when all child nodes was
	 * visited and on returning to the parent).
	 * 
	 * @param curElement
	 *            The element that was parsed.
	 */
	public abstract void afterNext(T curElement);

	/**
	 * Preprocesses the root node of the current subtree.
	 * 
	 * @param curElement
	 *            Current statement to process.
	 * @return True if child nodes should be visited, false otherwise.
	 */
	@SuppressWarnings("unchecked")
	public boolean visit(ASTNode curElement) {
		boolean retVal = true;
		if (this.validElement(curElement)) {
			retVal = this.preNext((T) curElement);
			compiler.preProcessedStatement(this);
		}
		return retVal;
	}

	/**
	 * Post processing of the root node of the current subtree.
	 * 
	 * @param curElement
	 *            Current statement to process.
	 */
	@SuppressWarnings("unchecked")
	public void endVisit(ASTNode curElement) {
		if (this.validElement(curElement)) {
			this.afterNext((T) curElement);
			compiler.postProcessedStatement();
		}
	}

	/**
	 * Creates the appropriate exporter implementation based on the type of the
	 * given node.
	 * 
	 * @param <T>
	 *            The node type which is parsed by the exporter.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ASTNode> ExporterBase<T> createExporter(ASTNode curElement, PlantUmlCompiler compiler) {
		switch (curElement.getNodeType()) {
		case ASTNode.TYPE_DECLARATION:
			return (ExporterBase<T>) new InteractionExporter(compiler);
		case ASTNode.METHOD_INVOCATION:
			return (ExporterBase<T>) MethodInvocationExporter.createExporter(curElement, compiler);
		case ASTNode.WHILE_STATEMENT:
		case ASTNode.FOR_STATEMENT:
		case ASTNode.ENHANCED_FOR_STATEMENT:
		case ASTNode.DO_STATEMENT:
			return (ExporterBase<T>) new LoopFragmentExporter(compiler);
		case ASTNode.IF_STATEMENT:
			return (ExporterBase<T>) new OptAltFragmentExporter(compiler);
		case ASTNode.BLOCK:
			return (ExporterBase<T>) new LifelineDeactivator(compiler);
		}

		return null;
	}

	public boolean skippedStatement(ASTNode node) {
		return !validElement(node);
	}

}
