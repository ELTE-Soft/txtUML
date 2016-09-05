package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;

public class PlantUmlCompiler extends ASTVisitor {
	private List<ASTNode> errors;
	private Stack<BaseSeqdiagExporter<? extends ASTNode>> expQueue;
	private List<MethodDeclaration> fragments;
	private List<String> activeLifelines;

	private String compiledOutput;
	private CompiledElementsCache cache;

	public PlantUmlCompiler(List<MethodDeclaration> fragments) {
		errors = new ArrayList<ASTNode>();
		this.fragments = fragments;
		activeLifelines = new ArrayList<String>();
		expQueue = new Stack<BaseSeqdiagExporter<? extends ASTNode>>();

		compiledOutput = "";
		cache = new CompiledElementsCache();
	}

	public String getCompiledOutput() {
		return compiledOutput;
	}

	@Override
	public boolean preVisit2(ASTNode node) {

		BaseSeqdiagExporter<?> exp = BaseSeqdiagExporter.createExporter(node, this);

		if (exp != null) {
			return exp.visit(node);
		}

		return true;
	};

	public static String getFullyQualifiedName(MethodDeclaration decl) {
		return decl.resolveBinding().getDeclaringClass().getQualifiedName().toString() + "."
				+ decl.getName().toString();
	}

	public String compile(String fullyQualifiedName) {

		if (this.cache.hasCompiledElement(fullyQualifiedName)) {
			return this.cache.getCompiledElement(fullyQualifiedName);
		}

		boolean hasDecl = false;
		MethodDeclaration declaration = null;

		for (MethodDeclaration decl : fragments) {
			if (PlantUmlCompiler.getFullyQualifiedName(decl).equals(fullyQualifiedName)) {
				hasDecl = true;
				declaration = decl;
				break;
			}
		}

		if (hasDecl) {
			PlantUmlCompiler compiler = new PlantUmlCompiler(this.fragments);
			declaration.accept(compiler);
			this.cache.addCompiledElement(PlantUmlCompiler.getFullyQualifiedName(declaration), compiler.getCompiledOutput());
			return compiler.getCompiledOutput();
		}
		
		return null;
	}

	@Override
	public void postVisit(ASTNode node) {
		BaseSeqdiagExporter<?> expt = BaseSeqdiagExporter.createExporter(node, this);

		if (!expQueue.isEmpty() && expt != null && !expt.skippedStatement(node)) {
			BaseSeqdiagExporter<?> exp = expQueue.peek();

			if (expt.getClass().isInstance(exp)) {
				exp.endVisit(node);
			} else if (!expt.getClass().isInstance(exp) && !expt.skippedStatement(node)) {
				errors.add(node);
			}
		}
	}

	public List<String> getErrors() {
		ArrayList<String> errList = new ArrayList<String>();
		for (ASTNode error : errors) {
			errList.add("Error! Couldn't parse the following statement: " + error.toString() + "\n");
		}

		if (!expQueue.isEmpty()) {
			errList.add("There was a total of " + expQueue.size() + "half-processed or badly processed statements");
		}

		return errList;
	}

	public void preProcessedStatement(BaseSeqdiagExporter<? extends ASTNode> exporter) {
		expQueue.push(exporter);
	}

	public void postProcessedStatement() {
		expQueue.pop();
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
			compiledOutput += "activate " + lifeline + "\n";
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
				compiledOutput += "deactivate " + lifeline + System.lineSeparator();
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

	public void println(String message) {
		this.compiledOutput += message + System.lineSeparator();
	}
}
