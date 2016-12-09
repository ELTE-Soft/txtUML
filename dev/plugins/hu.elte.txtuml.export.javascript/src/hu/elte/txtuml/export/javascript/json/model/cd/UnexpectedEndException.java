package hu.elte.txtuml.export.javascript.json.model.cd;

/**
 * 
 * An exception which occurs if an associations end can't be linked to the
 * EMF-UML model
 *
 */
public class UnexpectedEndException extends Exception {
	private static final long serialVersionUID = 5740270045444011092L;

	/**
	 * Constructor
	 * 
	 * @param endName
	 *            the name of the end
	 */
	public UnexpectedEndException(String endName) {
		super("Unexpected AttributeLink end: " + endName);
	}
}
