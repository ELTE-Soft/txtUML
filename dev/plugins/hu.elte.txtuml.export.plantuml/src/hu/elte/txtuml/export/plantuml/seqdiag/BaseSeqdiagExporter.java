package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

/**
 * 
 * @author Zoli
 *
 * @param <T>
 *            the nodeType which is parsed by the exporter
 */
public abstract class BaseSeqdiagExporter<T extends ASTNode> {

	protected PrintWriter targetFile;
	protected PlantUmlGenerator generator;

	public BaseSeqdiagExporter(PrintWriter targetFile, PlantUmlGenerator generator) {
		this.targetFile = targetFile;
		this.generator = generator;
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
	public abstract void preNext(T curElement);

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
	public boolean visit(T curElement) {
		if (this.validElement(curElement)) {
			this.preNext(curElement);
			generator.preProcessedStatement(this);
		}

		return true;
	}

	/**
	 * Post processing of the sub tree's root node
	 * 
	 * @param curElement
	 *            current statement to process
	 */
	public void endVisit(T curElement) {
		if (this.validElement(curElement)) {
			this.afterNext(curElement);
			generator.postProcessedStatement();
		}
	}
}
