package hu.elte.txtuml.importing.uml2;

public class ConvertException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

	public ConvertException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
