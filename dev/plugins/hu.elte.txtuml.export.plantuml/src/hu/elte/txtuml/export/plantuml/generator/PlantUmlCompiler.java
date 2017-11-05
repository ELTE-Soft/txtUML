package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;

/**
 * Main Compiler Class Only visits the run and initialize methods of the class
 * fragments are compiled when required.<br>
 * {@link CompileCache} caches the compiled fragment for reuse and the
 * {@link #compile(String) } method compiles it depending on a fully qualified
 * name. Furthermore handles lifeline de-activation and activation. <br>
 * for more information on the compilation see {@link BaseSeqdiagExporter}
 * 
 * @author Zoli
 *
 */
public class PlantUmlCompiler extends ASTVisitor {
	private List<ASTNode> errors;
	private Stack<BaseSeqdiagExporter<? extends ASTNode>> expQueue;
	private List<MethodDeclaration> fragments;
	private List<FieldDeclaration> lifelines;
	private List<String> activeLifelines;
	private String currentClassFullyQualifiedName;

	private HashMap<Integer, FieldDeclaration> fieldDeclarationOrder;
	private int lastPosFilled;

	private String compiledOutput;
	private CompiledElementsCache cache;

	/**
	 * flag to mark if we are compiling a fragment right now
	 */
	private boolean fragmentCompile = false;

	public PlantUmlCompiler(List<FieldDeclaration> lifelines, List<MethodDeclaration> fragments,
			boolean fragmentCompile) {
		errors = new ArrayList<ASTNode>();
		this.fragments = fragments;
		activeLifelines = new ArrayList<String>();
		expQueue = new Stack<BaseSeqdiagExporter<? extends ASTNode>>();

		compiledOutput = "";
		cache = new CompiledElementsCache();
		this.fragmentCompile = fragmentCompile;
		this.lifelines = lifelines;
		fieldDeclarationOrder = new HashMap<Integer, FieldDeclaration>();
		lastPosFilled = 0;
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

	/**
	 * In case we are visiting a TypeDeclaration we need to generate the
	 * participants too
	 */
	public boolean visit(TypeDeclaration decl) {
		currentClassFullyQualifiedName = decl.resolveBinding().getQualifiedName().toString();

		if (!fragmentCompile) {
			for (FieldDeclaration sfdecl : lifelines) {
				sfdecl.accept(this);
			}
		}

		return true;
	}

	/**
	 * Normally we only need to care if the method is the run or the initialize
	 * method we can skip the rest.<br>
	 * In case of fragment compilation we need to compile all method
	 * declarations( should be only the one we are compiling rigth now)
	 */
	public boolean visit(MethodDeclaration decl) {
		if (decl.resolveBinding().getDeclaringClass().getQualifiedName().toString()
				.equals(currentClassFullyQualifiedName) && decl.getName().toString().equals("run")
				|| decl.getName().toString().equals("initialize")) {
			return true;
		} else if (fragmentCompile) {
			return true;
		}
		return false;
	}

	/*
	 * Get fully qualified names of a method
	 */

	/**
	 * 
	 * @param decl
	 *            the {@link MethodDeclaration} we need the fully qualified name
	 *            of
	 * @return
	 */
	public static String getFullyQualifiedName(MethodDeclaration decl) {
		return decl.resolveBinding().getDeclaringClass().getQualifiedName().toString() + "."
				+ decl.getName().toString();
	}

	/**
	 * 
	 * @param invoc
	 *            the {@link MethodInvocation} we need the fully qualified name
	 *            of
	 * @return
	 */
	public static String getFullyQualifiedName(MethodInvocation invoc) {
		return invoc.resolveMethodBinding().getDeclaringClass().getQualifiedName().toString() + "."
				+ invoc.getName().toString();
	}

	/**
	 * get a compiled fragment from cache, or if it's not in the cache compile
	 * it.
	 * 
	 * @param fullyQualifiedName
	 *            the fully qualified name of the fragment we are compiling
	 * @return
	 */
	public String compile(String fullyQualifiedName) {

		if (this.cache.hasCompiledElement(fullyQualifiedName)) {
			CompileCache cache = this.cache.getCompiledElement(fullyQualifiedName);
			activeLifelines.addAll(cache.getActiveLifeines());
			return this.compiledOutput;
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
			PlantUmlCompiler compiler = new PlantUmlCompiler(this.lifelines, this.fragments, true);
			compiler.activeLifelines = activeLifelines;
			declaration.accept(compiler);
			this.cache.addCompiledElement(PlantUmlCompiler.getFullyQualifiedName(declaration),
					compiler.getCompiledOutput(), compiler.activeLifelines);
			activeLifelines.addAll(compiler.activeLifelines);
			return compiler.getCompiledOutput();
		}

		return null;
	}

	/**
	 * End of visit actions
	 */
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

	/**
	 * Get the compilation errors
	 * 
	 * @return
	 */
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

	/*
	 * compile time statement handling
	 */

	public void preProcessedStatement(BaseSeqdiagExporter<? extends ASTNode> exporter) {
		expQueue.push(exporter);
	}

	public void postProcessedStatement() {
		expQueue.pop();
	}

	/**
	 * 
	 * checks if lifeline is active (used in exporting)
	 * 
	 * @param lifeline
	 * @return true if the lifeline is activated
	 */
	public boolean lifelineIsActive(String lifeline) {
		return activeLifelines.contains(lifeline);
	}

	/**
	 * activates lifeline if it currently is not active and does the needed
	 * supplementary operations
	 * 
	 * @param lifeline
	 *            lifeline to activate
	 */
	public void activateLifeline(String lifeline) {
		if (!lifelineIsActive(lifeline)) {
			compiledOutput += "activate " + lifeline + "\n";
			activeLifelines.add(lifeline);
		}
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
			compiledOutput += "deactivate " + lifeline + System.lineSeparator();
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

	public String getCurrentClassName() {
		return this.currentClassFullyQualifiedName;
	}

	/*
	 * Lifeline order handling
	 */

	public int lastDeclaredParticipantID() {
		return this.lastPosFilled;
	}

	public void addToWaitingList(int id, FieldDeclaration element) {
		fieldDeclarationOrder.put(id, element);
	}

	public void compiledLifeline(int id) {
		this.lastPosFilled = id;
	}

	public void compileWaitingLifelines() {
		for (int key : fieldDeclarationOrder.keySet()) {
			if (key == this.lastPosFilled || key == this.lastPosFilled + 1) {
				fieldDeclarationOrder.get(key).accept(this);
			}
		}
	}
}
