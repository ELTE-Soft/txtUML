package hu.elte.txtuml.export.uml2.transform.backend;

/**
 * Exception used by the exporter.
 */
@SuppressWarnings("serial")
public class ExportException extends Exception {

	/**
	 * Creates an ExportException instance with the given error message.
	 * 
	 * @param msg
	 *            The error message.
	 */
	public ExportException(String msg) {
		super(msg);
	}

}
