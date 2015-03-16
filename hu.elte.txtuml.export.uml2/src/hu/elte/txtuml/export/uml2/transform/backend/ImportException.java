package hu.elte.txtuml.export.uml2.transform.backend;

@SuppressWarnings("serial")
public class ImportException extends Exception {
	public ImportException(String msg) {
		message = msg;
	}
	public String getMessage() {
		return message;
	}
	private String message;
}
