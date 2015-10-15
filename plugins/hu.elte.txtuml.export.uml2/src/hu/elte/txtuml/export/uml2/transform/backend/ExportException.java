package hu.elte.txtuml.export.uml2.transform.backend;

/**
 * Exception used by the exporter.
 * 
 * @author Adam Ancsin
 *
 */
@SuppressWarnings("serial")
public class ExportException extends Exception {

	/**
	 * Creates an ExportException instance with the given error message.
	 * 
	 * @param msg
	 *            The error message.
	 * @author Adam Ancsin
	 */
	public ExportException(String msg) {
		super(msg);
	}

}
