package hu.elte.txtuml.api.model.execution.impl.assoc;

/**
 * An exception raised and also caught in the model which indicates that some
 * multiplicity bounds have been offended.
 * <p>
 * Despite being a subclass of the {@link java.io.Serializable} interface
 * through {@link Exception}, this class does not provide a
 * <code>serialVersionUID</code> because serialization is never used on it.
 */
@SuppressWarnings("serial")
public class MultiplicityException extends Exception {

	/**
	 * Creates a new <code>MultiplicityException</code> without a message.
	 */
	public MultiplicityException() {
	}

}
