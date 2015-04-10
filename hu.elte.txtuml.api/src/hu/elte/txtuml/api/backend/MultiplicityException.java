package hu.elte.txtuml.api.backend;

@SuppressWarnings("serial")
public class MultiplicityException extends Exception {

	/**
	 * Creates a new <code>MultiplicityException</code> without a message.
	 */
	public MultiplicityException() {
		super();
	}

	/**
	 * Creates a new <code>MultiplicityException</code> with the specified
	 * message.
	 * 
	 * @param message
	 *            the message of this exception
	 */
	public MultiplicityException(String message) {
		super(message);
	}

}
