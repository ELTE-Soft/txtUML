package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.plantuml.seqdiag.ExporterBase;

/**
 * <b>Main PlantUML compiler class.</b>
 * <p>
 * Visits the run and initialize methods of the sequence diagram description
 * class. Also responsible for handling the exporters.<br>
 * Furthermore handles lifeline positions, activation and deactivation. <br>
 * For more information about the compilation, see {@link ExporterBase}.
 */
public class PlantUmlCompiler extends ASTVisitor {

	/**
	 * Preprocessed but not postprocessed subtree roots. If this stack is not
	 * empty after the compilation, manual postprocessing is needed, for example
	 * at the end of combined fragments, lifelines and interactions (see
	 * {@link #postVisit(ASTNode)}).
	 */
	private Stack<ExporterBase<? extends ASTNode>> exporterQueue;
	private List<FieldDeclaration> lifelines;
	private List<String> activeLifelines;
	private List<ASTNode> errors;
	private String currentClassFullyQualifiedName;
	private PriorityQueue<QueueElem> declaredLifelines;

	private StringBuilder compiledOutput;

	public PlantUmlCompiler(final List<FieldDeclaration> lifelines) {
		errors = new ArrayList<ASTNode>();
		activeLifelines = new ArrayList<String>();
		exporterQueue = new Stack<ExporterBase<? extends ASTNode>>();
		declaredLifelines = new PriorityQueue<>(Comparator.comparing(QueueElem::getPriority));

		compiledOutput = new StringBuilder();
		this.lifelines = lifelines;
	}

	/**
	 * Returns the compiled output.
	 */
	public String getCompiledOutput() {
		return compiledOutput.toString();
	}

	@Override
	public boolean preVisit2(ASTNode node) {
		ExporterBase<?> exp = ExporterBase.createExporter(node, this);
		if (exp != null) {
			return exp.visit(node);
		}
		return true;
	};

	/**
	 * In case we are visiting a TypeDeclaration we need to generate the
	 * participants too.
	 */
	@Override
	public boolean visit(TypeDeclaration decl) {
		currentClassFullyQualifiedName = decl.resolveBinding().getQualifiedName().toString();
		lifelines.forEach(lifeline -> lifeline.accept(this));
		return true;
	}

	/**
	 * Normally we only need to care if the method is the run or the initialize
	 * method, we can skip the rest.
	 */
	@Override
	public boolean visit(MethodDeclaration decl) {
		return decl.resolveBinding().getDeclaringClass().getQualifiedName().toString()
				.equals(currentClassFullyQualifiedName)
				&& (decl.getName().toString().equals("run") || decl.getName().toString().equals("initialize"));
	}

	/**
	 * End of visit actions. (For the end of combined fragments, lifelines and
	 * interactions.)
	 */
	@Override
	public void postVisit(ASTNode node) {
		if (!exporterQueue.isEmpty()) {
			ExporterBase<?> expt = ExporterBase.createExporter(node, this);

			if (expt != null && !expt.skippedStatement(node)) {
				ExporterBase<?> exp = exporterQueue.peek();

				if (expt.getClass().isInstance(exp)) {
					exp.endVisit(node);
				} else if (!expt.getClass().isInstance(exp) && !expt.skippedStatement(node)) {
					errors.add(node);
				}
			}
		}
	}

	/**
	 * Returns the list of compilation errors.
	 */
	public List<String> getErrors() {
		ArrayList<String> errList = new ArrayList<String>();
		for (ASTNode error : errors) {
			errList.add("Error! Couldn't parse the following statement: " + error.toString() + "\n");
		}

		if (!exporterQueue.isEmpty()) {
			errList.add(
					"There was a total of " + exporterQueue.size() + " half-processed or badly processed statements.");
		}

		return errList;
	}

	/*
	 * compile time statement handling
	 */

	/**
	 * Called when a subtree root has been preprocessed.
	 * 
	 * @param exporter
	 *            The subtree root.
	 */
	public void preProcessedStatement(ExporterBase<? extends ASTNode> exporter) {
		exporterQueue.push(exporter);
	}

	/**
	 * Called when the recently added subtree root has been postprocessed.
	 */
	public void postProcessedStatement() {
		exporterQueue.pop();
	}

	/**
	 * Checks whether the given lifeline is active. This method is called during
	 * exporting.
	 * 
	 * @param lifeline
	 *            The lifeline.
	 * @return True if the lifeline is activated, false otherwise.
	 */
	private boolean isLifelineActive(String lifeline) {
		return activeLifelines.contains(lifeline);
	}

	/**
	 * Activates the given lifeline if it is not currently active.
	 * 
	 * @param lifeline
	 *            The lifeline to activate.
	 */
	public void activateLifeline(String lifeline) {
		if (!isLifelineActive(lifeline)) {
			activeLifelines.add(lifeline);
			compiledOutput.append("activate ");
			compiledOutput.append(lifeline);
			compiledOutput.append(System.lineSeparator());
		}
	}

	/**
	 * Deactivates the given lifeline if it is currently active.
	 * 
	 * @param lifeline
	 *            The lifeline to deactivate.
	 */
	public void deactivateLifeline(String lifeline) {
		if (isLifelineActive(lifeline)) {
			activeLifelines.remove(lifeline);
			compiledOutput.append("deactivate ");
			compiledOutput.append(lifeline);
			compiledOutput.append(System.lineSeparator());
		}
	}

	/**
	 * Deactivates all active lifelines.
	 */
	public void deactivateAllLifelines() {
		while (!activeLifelines.isEmpty()) {
			deactivateLifeline(activeLifelines.get(0));
		}
	}

	/**
	 * Prints the given message to the compiled output.
	 * 
	 * @param message
	 *            Output message in PlantUML format.
	 */
	public void println(String message) {
		compiledOutput.append(message);
		compiledOutput.append(System.lineSeparator());
	}

	/*
	 * Lifeline position order handling
	 */

	public void addLifeline(int position, FieldDeclaration lifeline) {
		QueueElem e = new QueueElem();
		e.setLifelineDecl(lifeline);
		e.setPriority(position);
		declaredLifelines.offer(e);
	}

	public void printLifelines() {
		while (!declaredLifelines.isEmpty()) {
			QueueElem elem = declaredLifelines.poll();
			println("participant " + elem.getLifelineDecl().fragments().get(0).toString());
		}
	}

	private class QueueElem {
		private int priority;
		private FieldDeclaration lifelineDecl;

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public FieldDeclaration getLifelineDecl() {
			return lifelineDecl;
		}

		public void setLifelineDecl(FieldDeclaration lifelineDecl) {
			this.lifelineDecl = lifelineDecl;
		}
	}

}
