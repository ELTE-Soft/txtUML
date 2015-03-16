package hu.elte.txtuml.layout.export;

import java.util.List;

import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;

public class DiagramExportationReport { // FIXME add set of nodes and set of links as result parameters

	private StatementList statements;
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
	 * @return The number of errors occurred during exportation.
	 */
	public final int getErrorCount() {
		return errors;
	}

	/**
	 * @return The number of warnings occurred during exportation.
	 */
	public final int getWarningCount() {
		return warnings;
	}

	/**
	 * If the <code>isSuccesful</code> method returns <code>false</code>, the return value of
	 * this method should not be used (it is probably <code>null</code>).
	 * 
	 * @return The statement list created as the result of the exportation.
	 */
	public final List<Statement> getStatements() {
		return statements;
	}

	/**
	 * Overridable method to log a warning. Called for every warning during
	 * exportation.
	 * 
	 * @param message
	 */
	protected void logWarning(String message) {
		System.err.println("Warning: " + message);
	}

	/**
	 * Overridable method to log an error. Called for every error during
	 * exportation.
	 * 
	 * @param message
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
		errors = 0;
		warnings = 0;
		statements = null;
	}

	/**
	 * Should be called only by the diagram exporter.
	 * 
	 * @param statements
	 *            The statement list created as the result of the exportation.
	 */
	public final void setResult(StatementList statements) {
		this.statements = statements;
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
