package hu.elte.txtuml.seqdiag.export.plantuml.exceptions;

public class ExportRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3558735956015246327L;

	public ExportRuntimeException(String message) {
		super(message);
	}

	public ExportRuntimeException(String message, Exception cause) {
		super(message,cause);
	}

}
