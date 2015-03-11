package hu.elte.txtuml.layout.export;

import java.util.List;

import Annotations.Statement;
import hu.elte.txtuml.layout.export.interfaces.StatementList;

public class DiagramExportationReport {

	private StatementList statements;
	private int errors;
	private int warnings;

	public DiagramExportationReport() {
		clear();
	}

	/**
	 * The exportation is successful if no errors have occurred. If it is
	 * successful, getResult() returns the expected list of statements.
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
	 * If isSuccesful() returns false, the return value of this method should
	 * not be used (it is probably null).
	 * 
	 * @return The statement list created as the result of the exportation.
	 */
	public final List<Statement> getResult() {
		return statements;
	}

	/**
	 * Overridable method to log a warning. Called for every warning during exportation.
	 * 
	 * @param message
	 */
	protected void logWarning(String message) {
		System.err.println("Warning: " + message);
	}
	
	/**
	 * Overridable method to log an error. Called for every error during exportation.
	 * 
	 * @param message
	 */
	protected void logError(String message) {
		System.err.println("Error: " + message);
	}
	
	/**
	 * Should only be called by the diagram exporter.
	 * 
	 * Resets fields to their default value;
	 */
	public final void clear() {
		errors = 0;
		warnings = 0;
		statements = null;
	}
	
	/**
	 * Should only be called by the diagram exporter. 
	 * 
	 * @param statements
	 *            The statement list created as the result of the exportation.
	 */
	public final void setResult(StatementList statements) {
		this.statements = statements;
	}

	/**
	 * Should only be called by the diagram exporter.
	 * 
	 * Registers and logs a new warning.
	 * 
	 * @param message
	 */
	public final void warning(String message) {
		++warnings;
		logWarning(message);
	}

	/**
	 * Should only be called by the diagram exporter.
	 * 
	 * Registers and logs a new error.
	 * 
	 * @param message
	 */
	public final void error(String message) {
		++errors;
		logError(message);
	}

}
