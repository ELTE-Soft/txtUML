package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;
import hu.elte.txtuml.export.plantuml.seqdiag.fragments.LoopFragment;
import hu.elte.txtuml.export.plantuml.seqdiag.fragments.OptAltFragment;

/**
 * 
 * @author Zoli
 *
 * @param <T>
 *            the nodeType which is parsed by the exporter
 */
public abstract class BaseSeqdiagExporter<T extends ASTNode> {

	protected PlantUmlCompiler compiler;

	public BaseSeqdiagExporter(PlantUmlCompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * 
	 * @param curElement
	 *            current element to be parsed
	 * @return the received node is parseable by this exporter if true returned
	 */
	public abstract boolean validElement(ASTNode curElement);

	/**
	 * What to do when first visited(not yet seen)
	 * 
	 * @param curElement
	 *            current element to be parsed
	 */
	public abstract boolean preNext(T curElement);

	/**
	 * What to do on the end of the visit(when all child nodes was visited and
	 * on returning to the parent)
	 * 
	 * @param curElement
	 *            the element that was parsed
	 */
	public abstract void afterNext(T curElement);

	/**
	 * Pre processing of the current sub-tree's root node
	 * 
	 * @param curElement
	 *            current statement to process
	 * @return true if the child-nodes should be visited
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
	 * Post processing of the sub tree's root node
	 * 
	 * @param curElement
	 *            current statement to process
	 */
	@SuppressWarnings("unchecked")
	public void endVisit(ASTNode curElement) {
		if (this.validElement(curElement)) {
			this.afterNext((T) curElement);
			compiler.postProcessedStatement();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ASTNode> BaseSeqdiagExporter<T> createExporter(ASTNode curElement,
			PlantUmlCompiler compiler) {
		switch (curElement.getNodeType()) {
		case ASTNode.TYPE_DECLARATION:
			return (BaseSeqdiagExporter<T>) new InteractionExporter(compiler);
		case ASTNode.FIELD_DECLARATION:
			return (BaseSeqdiagExporter<T>) new LifelineExporter(compiler);
		case ASTNode.METHOD_INVOCATION:
			return (BaseSeqdiagExporter<T>) MethodInvocationExporter.createExporter(curElement, compiler);
		case ASTNode.WHILE_STATEMENT:
		case ASTNode.FOR_STATEMENT:
		case ASTNode.ENHANCED_FOR_STATEMENT:
			return (BaseSeqdiagExporter<T>) new LoopFragment(compiler);
		case ASTNode.IF_STATEMENT:
			return (BaseSeqdiagExporter<T>) new OptAltFragment(compiler);
		case ASTNode.BLOCK:
			return (BaseSeqdiagExporter<T>) new LifelineDeactivator(compiler);
		}

		return null;
	}

	public boolean skippedStatement(ASTNode node) {
		return !validElement(node);
	}
}
