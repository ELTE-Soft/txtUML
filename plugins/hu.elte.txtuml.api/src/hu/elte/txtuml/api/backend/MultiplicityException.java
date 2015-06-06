package hu.elte.txtuml.api.backend;

/**
 * An exception raised and also caught in the model which indicates that some
 * multiplicity bounds have been offended.
 * <p>
 * Despite being a subclass of the {@link java.io.Serializable} interface
 * through {@link Exception}, this class does not provide a
 * <code>serialVersionUID</code> because serialization is never used on it.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
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
