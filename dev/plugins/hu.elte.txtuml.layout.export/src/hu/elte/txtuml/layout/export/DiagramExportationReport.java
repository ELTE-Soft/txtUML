package hu.elte.txtuml.layout.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

/**
 * The type to be returned by the exportation of a diagram layout description.
 * Provides important information about the exportation, including its results.
 */
public class DiagramExportationReport {

	private DiagramType type;
	private String modelName;
	private String referencedElementName; 
	private StatementList statements;
	private Set<RectangleObject> nodes;
	private Set<LineAssociation> links;
	private int errorCount;
	private int warningCount;
	private List<String> errors;
	private List<String> warnings;

	public DiagramExportationReport() {
		clear();
	}

	/**
	 * Returns whether the exportation was successful or not. The exportation is
	 * successful if no errors have occurred.
	 */
	public final boolean isSuccessful() {
		return errorCount == 0;
	}

	/**
	 * Returns the string representation of the root element (a subclass of
	 * Model in the current implementation) which the exported diagram belongs
	 * to.
	 * <p>
	 * If the root element couldn't be determined (eg. the diagram description
	 * was empty), the return value is <code>null</code>. If the
	 * <code>isSuccessful</code> method returns <code>false</code>, the return
	 * value of this method should not be used (it is probably <code>null</code>
	 * ).
	 */
	public final String getModelName() {
		return modelName;
	}
	
	
	/**
	 * Returns the string representation of the element which the exported diagram belongs
	 * to. (eg. The referenced model element in case of a class diagram is package,
	 * in case of a statemachine it is a class) 
	 * @return - the fully qualified name of the referenced element
	 */
	public final String getReferencedElementName(){
		return referencedElementName;
	}

	/**
	 * @return the number of errors occurred during exportation
	 */
	public final int getErrorCount() {
		return errorCount;
	}

	/**
	 * @return the number of warnings occurred during exportation
	 */
	public final int getWarningCount() {
		return warningCount;
	}

	/**
	 * If the <code>isSuccessful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>Unknown</code>).
	 * 
	 * @return the type of the exported diagram
	 */
	public final DiagramType getType() {
		return type;
	}

	/**
	 * If the <code>isSuccessful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>null</code>).
	 * 
	 * @return the statement list created as the result of the exportation
	 */
	public final List<Statement> getStatements() {
		return statements;
	}

	/**
	 * If the <code>isSuccessful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>null</code>).
	 * 
	 * @return the set of nodes created as the result of the exportation
	 */
	public final Set<RectangleObject> getNodes() {
		return nodes;
	}

	/**
	 * If the <code>isSuccessful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>null</code>).
	 * 
	 * @return the set of links created as the result of the exportation
	 */
	public final Set<LineAssociation> getLinks() {
		return links;
	}

	/**
	 * @return the (possibly empty) list of errors occurred during the
	 *         exportation
	 */
	public final List<String> getErrors() {
		return errors;
	}

	/**
	 * @return the (possibly empty) list of warnings occurred during the
	 *         exportation
	 */
	public final List<String> getWarnings() {
		return warnings;
	}

	/**
	 * Overridable method to log a warning. Called for every warning during
	 * exportation.
	 * 
	 * @param message
	 *            the message to show
	 */
	protected void logWarning(String message) {
		warnings.add(message);
	}

	/**
	 * Overridable method to log an error. Called for every error during
	 * exportation.
	 * 
	 * @param message
	 *            the message to show
	 */
	protected void logError(String message) {
		errors.add(message);
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * Resets fields to their default value;
	 */
	public final void clear() {
		modelName = null;
		referencedElementName = null;
		type = DiagramType.Unknown;
		errorCount = 0;
		warningCount = 0;
		errors = new ArrayList<String>();
		warnings = new ArrayList<String>();
		statements = null;
		nodes = null;
		links = null;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param modelName
	 *            string representation of the diagram's root element
	 */
	public final void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param referencedElementName
	 *            string representation of the diagram's referenced element
	 */
	public final void setReferencedElementName(String referencedElementName) {
		this.referencedElementName = referencedElementName;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param type
	 *            the type of the exported diagram
	 */
	public final void setType(DiagramType type) {
		this.type = type;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param statements
	 *            the statement list created as the result of the exportation
	 */
	public final void setStatements(StatementList statements) {
		this.statements = statements;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param nodes
	 *            the set of nodest created as the result of the exportation
	 */
	public final void setNodes(Set<RectangleObject> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param links
	 *            the set of links created as the result of the exportation
	 */
	public final void setLinks(Set<LineAssociation> links) {
		this.links = links;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * Registers and logs a new warning.
	 */
	public final void warning(String message) {
		++warningCount;
		logWarning(message);
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * Registers and logs a new error.
	 */
	public final void error(String message) {
		++errorCount;
		logError(message);
	}

}
