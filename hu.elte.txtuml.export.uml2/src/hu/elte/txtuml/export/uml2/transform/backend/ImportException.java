package hu.elte.txtuml.export.uml2.transform.backend;

/**
 * Exception used by the importer.
 * @author Adam Ancsin
 *
 */
@SuppressWarnings("serial")
public class ImportException extends Exception {
	
	/**
	 * Creates an ImportException instance with the given error message.
	 * @param msg The error message.
	 * @author Adam Ancsin
	 */
	public ImportException(String msg) {
		message = msg;
	}
	/**
	 * Gets the error message.
	 * @return The error message.
	 * @author Adam Ancsin
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * The error message.
	 */
	private String message;
}
