package hu.elte.txtuml.export.plantuml.exceptions;

/**
 * Structural error exception for the case when there is some internal compiler
 * error.
 */
public class SequenceDiagramStructuralException extends SequenceDiagramExportException {

	private static final long serialVersionUID = 4985302244057281568L;

	public SequenceDiagramStructuralException(String message) {
		super(message);
	}

}
