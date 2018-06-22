package hu.elte.txtuml.seqdiag.export.plantuml.exceptions;

/**
 * Error during the precompilation phase of sequence diagram export.
 */
public class PreCompilationError extends SequenceDiagramExportException {

	private static final long serialVersionUID = -1377577890930305456L;

	public PreCompilationError(String message) {
		super(message);
	}

}
