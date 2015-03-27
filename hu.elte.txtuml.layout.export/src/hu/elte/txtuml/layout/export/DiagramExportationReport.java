package hu.elte.txtuml.layout.export;

import java.util.List;
import java.util.Set;

import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * The type to be returned by the exportation of a diagram layout description.
 * Provides important information about the exportation, including its results.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class DiagramExportationReport {

	private DiagramType type;
	private StatementList statements;
	private Set<RectangleObject> nodes;
	private Set<LineAssociation> links;
	private int errors;
	private int warnings;

	public DiagramExportationReport() {
		clear();
	}

	/**
	 * Returns whether the exportation was successful or not. The exportation is
	 * successful if no errors have occurred.
	 */
	public final boolean isSuccessful() {
		return errors == 0;
	}

	/**
	 * @return the number of errors occurred during exportation
	 */
	public final int getErrorCount() {
		return errors;
	}

	/**
	 * @return the number of warnings occurred during exportation
	 */
	public final int getWarningCount() {
		return warnings;
	}

	/**
	 * If the <code>isSuccesful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>Unknown</code>).
	 * 
	 * @return the type of the exported diagram
	 */
	public final DiagramType getType() {
		return type;
	}

	/**
	 * If the <code>isSuccesful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>null</code>).
	 * 
	 * @return the statement list created as the result of the exportation
	 */
	public final List<Statement> getStatements() {
		return statements;
	}

	/**
	 * If the <code>isSuccesful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>null</code>).
	 * 
	 * @return the set of nodes created as the result of the exportation
	 */
	public final Set<RectangleObject> getNodes() {
		return nodes;
	}

	/**
	 * If the <code>isSuccesful</code> method returns <code>false</code>, the
	 * return value of this method should not be used (it is probably
	 * <code>null</code>).
	 * 
	 * @return the set of links created as the result of the exportation
	 */
	public final Set<LineAssociation> getLinks() {
		return links;
	}

	/**
	 * Overridable method to log a warning. Called for every warning during
	 * exportation.
	 * 
	 * @param message
	 *            the message to show
	 */
	protected void logWarning(String message) {
		System.err.println("Warning: " + message);
	}

	/**
	 * Overridable method to log an error. Called for every error during
	 * exportation.
	 * 
	 * @param message
	 *            the message to show
	 */
	protected void logError(String message) {
		System.err.println("Error: " + message);
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * Resets fields to their default value;
	 */
	public final void clear() {
		type = DiagramType.Unknown;
		errors = 0;
		warnings = 0;
		statements = null;
		nodes = null;
		links = null;
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
		++warnings;
		logWarning(message);
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * Registers and logs a new error.
	 */
	public final void error(String message) {
		++errors;
		logError(message);
	}

}
