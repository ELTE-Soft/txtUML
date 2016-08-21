package hu.elte.txtuml.export.plantuml.exceptions;

/**
 * 
 * @author Zoli
 * 
 *         Structural error exception for the case when there is some internal
 *         error
 *
 */
public class SequenceDiagramStructuralException extends Exception {

	private static final long serialVersionUID = 4985302244057281568L;

	public SequenceDiagramStructuralException(String message) {
		super(message);
	}
}
