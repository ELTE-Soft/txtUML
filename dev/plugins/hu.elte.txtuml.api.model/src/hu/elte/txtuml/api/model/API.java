package hu.elte.txtuml.api.model;

/**
 * Provides methods to communicate with a txtUML model safely from the
 * <b>outside world</b>.
 * 
 * <p>
 * <b>Represents:</b> no model element, can only be used from outside the model
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Cannot be used in a model, call its static methods from any outside code (it
 * is always safe).
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface API {

	/**
	 * Asynchronously sends the specified signal to the specified target object.
	 * <p>
	 * Does not check whether the target object is deleted, it is only checked
	 * when the signal arrives to the object.
	 * 
	 * @param signal
	 *            the signal object to send
	 * @param target
	 *            the model object which will receive the signal
	 * 
	 * @throws NullPointerException
	 *             if {@code target} is {@code null}
	 */
	@ExternalBody
	static void send(Signal signal, ModelClass target) {
		Action.send(signal, target);
	}

	/**
	 * Logs a message.
	 * 
	 * @param message
	 *            the message to be logged
	 */
	@ExternalBody
	static void log(String message) {
		Action.log(message);
	}

	/**
	 * Logs an error message.
	 * 
	 * @param message
	 *            the error message to be logged
	 */
	@ExternalBody
	static void logError(String message) {
		Action.logError(message);
	}

}
