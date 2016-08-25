package hu.elte.txtuml.export.plantuml.generator;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;
import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;

public class PlantUmlGenerator {

	private PrintWriter targetFile;
	private CompilationUnit sourceCU;

	private MethodStatementWalker walker;
	private ArrayList<String> activeLifelines;
	private Stack<BaseSeqdiagExporter<? extends ASTNode>> expQueue;

	public PlantUmlGenerator(IFile targetFile, CompilationUnit source) {

		ByteArrayInputStream stream = new ByteArrayInputStream("".getBytes());

		try {
			targetFile.create(stream, false, null);
			this.targetFile = new PrintWriter(targetFile.getLocation().toOSString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.sourceCU = source;
		this.activeLifelines = new ArrayList<String>();
		expQueue = new Stack<BaseSeqdiagExporter<? extends ASTNode>>();
	}

	public void generate() throws SequenceDiagramStructuralException {

		walker = new MethodStatementWalker(this);
		sourceCU.accept(walker);

		if (walker.getErrors().size() > 0) {
			String errString = "";

			for (String error : walker.getErrors()) {
				errString += error;
			}

			if (!expQueue.isEmpty()) {
				errString += "Warning! Some expressions where left unparsed/unclosed \n";
			}
			throw new SequenceDiagramStructuralException(errString);
		}

		targetFile.flush();
		targetFile.close();

	}

	PrintWriter getTargetFile() {
		return this.targetFile;
	}

	/**
	 * 
	 * checks if lifeline is active( used in exporting)
	 * 
	 * @param lifeline
	 * @return true if the lifeline is activated
	 */
	public boolean lifelineIsActive(String lifeline) {
		if (activeLifelines.contains(lifeline)) {
			return true;
		}

		return false;
	}

	/**
	 * activates lifeline and does the needed supplementary operations
	 * 
	 * @param lifeline
	 *            lifeline to activate
	 */
	public void activateLifeline(String lifeline) {

		if (!lifelineIsActive(lifeline)) {
			targetFile.println("activate " + lifeline);
		}

		activeLifelines.add(lifeline);
	}

	/**
	 * 
	 * deactivates lifeline and does the needed supplementary operations
	 * 
	 * @param lifeline
	 *            lifeline to deactivate
	 */
	public void deactivateLifeline(String lifeline) {
		if (lifelineIsActive(lifeline)) {
			activeLifelines.remove(lifeline);

			if (!lifelineIsActive(lifeline)) {
				targetFile.println("deactivate " + lifeline);
			}
		}
	}

	/**
	 * deactivates all active lifelines
	 */
	public void deactivateAllLifelines() {
		while (activeLifelines.size() > 0) {
			this.deactivateLifeline(activeLifelines.get(0));
		}
	}

	public void preProcessedStatement(BaseSeqdiagExporter<? extends ASTNode> exporter) {
		expQueue.push(exporter);
	}

	public BaseSeqdiagExporter<? extends ASTNode> postProcessingStatement() {
		return expQueue.peek();
	}

	public void postProcessedStatement() {
		expQueue.pop();
	}
}
