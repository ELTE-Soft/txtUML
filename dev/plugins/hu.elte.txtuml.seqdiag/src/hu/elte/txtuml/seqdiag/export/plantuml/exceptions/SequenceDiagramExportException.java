package hu.elte.txtuml.seqdiag.export.plantuml.exceptions;

/**
 * General exception during sequence diagram export.
 */
public class SequenceDiagramExportException extends Exception {

	private static final long serialVersionUID = 1149563660203013809L;

	public SequenceDiagramExportException(String message) {
		super(message);
	}

}
