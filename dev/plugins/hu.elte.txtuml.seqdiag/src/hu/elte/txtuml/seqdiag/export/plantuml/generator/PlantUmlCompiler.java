package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.seqdiag.export.plantuml.exporters.ExporterBase;

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
	private final String seqDiagramName;

	private StringBuilder compiledOutput;
	
	/**
	 * Every private function will have its own context for its own parameter names as lifeline names
	 */
    public static Map<String, HashMap<String, Collection<String>>> lifelineNamesInContexts;
	

	public PlantUmlCompiler(final List<Lifeline> orderedLifelines, String seqDiagramName) {
		errors = new ArrayList<ASTNode>();
		exporterQueue = new Stack<ExporterBase<? extends ASTNode>>();
		this.orderedLifelines = orderedLifelines;
		this.seqDiagramName = seqDiagramName;
        PlantUmlCompiler.lifelineNamesInContexts = new HashMap<String, HashMap<String, Collection<String>>>();
        
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
		if (decl.resolveBinding().getName().equals(seqDiagramName)) {
			currentClassFullyQualifiedName = decl.resolveBinding().getQualifiedName().toString();
			orderedLifelines.stream().map(Lifeline::getLifelineDeclaration).forEach(lifeline -> lifeline.accept(this));
			return true;
		}
		return false;
	}

	/**
	 * Normally we only need to care if the method is the run or the initialize
	 * method, we can skip the rest.
	 */
	@Override
	public boolean visit(MethodDeclaration decl) {
		printLifelines();
		boolean isInitMethod = decl.getName().toString().equals("initialize");
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
			println("activate " + lifelineName);
		}
	}

	/**
	 * Deactivates all active lifelines.
	 */
	public void deactivateAllLifelines() {
		orderedLifelines.forEach(this::deactivateLifeline);
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

	/**
	 * Deactivates the given lifeline if it is currently active.
	 * 
	 * @param lifeline
	 *            The lifeline to deactivate.
	 */
	private void deactivateLifeline(Lifeline lifeline) {
		if (lifeline.isActive()) {
			lifeline.activate(false);
			println("deactivate " + lifeline.getName());
		}
	}

	/*
	 * Used for lifeline position order handling.
	 */
	private void printLifelines() {
		orderedLifelines.stream().filter(elem -> elem.getPriority() != -1).forEach(elem -> {
			println("participant " + elem.getName());
			elem.processed();
		});
	}

	/**
	 * Returns the name of the sequence diagram which is being exported
	 */
	public String getSeqDiagramName() {
		return seqDiagramName;
	}

    /**
     * This method updates the lifeline names and it's registered list of aliases upon a method call in the sequence diagram
     * @param arguments - the original objectiveNames
     * @param parameters - the possible new parameterNames
     */
    public void updateLifeLineNames(String methodName, List<Expression> arguments, List<SingleVariableDeclaration> parameters) {

        List<Integer> idxs = new ArrayList<>();
        for (int i = 0; i < arguments.size(); ++i) {
            if (arguments.get(i) instanceof Name) {
                idxs.add(i);
            }
        }
        
        List<String> oldNames = idxs.stream().map(i -> arguments.get(i)).map(elem -> ((Name) elem))
                .map(elem -> elem.toString()).collect(Collectors.toList());

        List<String> newNames = idxs.stream().map(i -> parameters.get(i)).map(elem -> ((SingleVariableDeclaration) elem))
                .map(elem -> elem.getName().toString()).collect(Collectors.toList());
        
        HashMap<String, Collection<String>> methodContext;
        
        if(!lifelineNamesInContexts.containsKey(methodName)) {
        	// we encountered this methodContext the first time
        	HashMap<String, Collection<String>> innerMapForMethodContext = new HashMap<String, Collection<String>>();
        	lifelineNamesInContexts.put(methodName, innerMapForMethodContext);
        }
        
        methodContext = lifelineNamesInContexts.get(methodName);
        
        for (int i = 0; i < oldNames.size(); ++i) {
            String oldName = oldNames.get(i);
            
            Collection<String> nameList = methodContext.get(oldName);
            // we encountered this LifeLine the first time
            if(nameList == null) {
            	methodContext.put(oldName, new ArrayList<String>());
            }
            
            // simply add the alias to the list
            methodContext.get(oldName).add(newNames.get(i));
        }
    }
}
