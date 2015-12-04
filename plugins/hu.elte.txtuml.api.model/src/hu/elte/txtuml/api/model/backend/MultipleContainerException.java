package hu.elte.txtuml.api.model.backend;

/**
 * An exception raised and also caught in the model which indicates that a
 * single object would become contained in two different containers. That is
 * caused by the <code>Action.link</code> calls of the same object with two
 * composition associations.
 * <p>
 * Despite being a subclass of the {@link java.io.Serializable} interface
 * through {@link Exception}, this class does not provide a
 * <code>serialVersionUID</code> because serialization is never used on it.
 *
 */

@SuppressWarnings("serial")
public class MultipleContainerException extends Exception {

	/**
	 * Creates a new <code>MultipleContainerException</code> without a message.
	 */
	public MultipleContainerException() {
	}

}
