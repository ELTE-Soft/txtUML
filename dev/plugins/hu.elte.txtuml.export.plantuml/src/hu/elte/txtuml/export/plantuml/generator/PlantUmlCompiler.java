package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
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
	private List<ASTNode> errors;
	private String currentClassFullyQualifiedName;
	private List<Lifeline> orderedLifelines;

	private StringBuilder compiledOutput;

	public PlantUmlCompiler(final List<Lifeline> orderedLifelines) {
		errors = new ArrayList<ASTNode>();
		exporterQueue = new Stack<ExporterBase<? extends ASTNode>>();
		this.orderedLifelines = orderedLifelines;

		compiledOutput = new StringBuilder();
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
		orderedLifelines.stream().map(Lifeline::getLifelineDeclaration).forEach(lifeline -> lifeline.accept(this));
		return true;
	}

	/**
	 * Normally we only need to care if the method is the run or the initialize
	 * method, we can skip the rest.
	 */
	@Override
	public boolean visit(MethodDeclaration decl) {
		boolean isInitMethod = decl.getName().toString().equals("initialize");
		if (isInitMethod) {
			printLifelines();
		}
		return decl.resolveBinding().getDeclaringClass().getQualifiedName().toString()
				.equals(currentClassFullyQualifiedName) && (decl.getName().toString().equals("run") || isInitMethod);
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
	 * Returns a lifeline with the given name.
	 * 
	 * @param lifelineName
	 *            The name of the lifeline.
	 * @return The found lifeline if the given name is valid, empty optional
	 *         otherwise.
	 */
	private Optional<Lifeline> getLifelineByName(String lifelineName) {
		return orderedLifelines.stream().filter(lifeline -> lifeline.getName().equals(lifelineName)).findFirst();
	}

	/**
	 * Activates the given lifeline if it is not currently active.
	 * 
	 * @param lifelineName
	 *            The lifeline to activate.
	 */
	public void activateLifeline(String lifelineName) {
		Optional<Lifeline> lifeline = getLifelineByName(lifelineName);
		if (lifeline.isPresent() && !lifeline.get().isActive()) {
			lifeline.get().activate(true);
			compiledOutput.append("activate ");
			compiledOutput.append(lifelineName);
			compiledOutput.append(System.lineSeparator());
		}
	}

	/**
	 * Deactivates the given lifeline if it is currently active.
	 * 
	 * @param lifelineName
	 *            The lifeline to deactivate.
	 */
	public void deactivateLifeline(String lifelineName) {
		Optional<Lifeline> lifeline = getLifelineByName(lifelineName);
		if (lifeline.isPresent() && lifeline.get().isActive()) {
			lifeline.get().activate(false);
			compiledOutput.append("deactivate ");
			compiledOutput.append(lifelineName);
			compiledOutput.append(System.lineSeparator());
		}
	}

	/**
	 * Deactivates all active lifelines.
	 */
	public void deactivateAllLifelines() {
		orderedLifelines.forEach(lifeline -> lifeline.activate(false));
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
	 * Used for lifeline position order handling.
	 */
	private void printLifelines() {
		orderedLifelines.stream().filter(elem -> elem.getPriority() != -1)
				.forEach(elem -> println("participant " + elem.getName()));
	}

}
