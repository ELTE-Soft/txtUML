package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.AbstractGeneralCollection.Builder;
import hu.elte.txtuml.utils.Logger;

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
 * is always thread-safe).
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@External
public abstract class API {

	/**
	 * Asynchronously sends the specified signal to the specified target object.
	 * <p>
	 * Does not check whether the target object is deleted, it is only checked
	 * when the signal arrives to the object.
	 * <p>
	 * Use {@link Action#send(Signal, ModelClass)} instead of this method from
	 * inside the model.
	 * 
	 * @param signal
	 *            the signal object to send
	 * @param target
	 *            the model object which will receive the signal
	 * 
	 * @throws NullPointerException
	 *             if {@code target} is {@code null}
	 */
	public static void send(Signal signal, ModelClass target) {
		target.runtime().receiveLaterViaAPI(signal);
	}

	/**
	 * Logs a message.
	 * <p>
	 * Use {@link Action#log(String)} instead of this method from inside the
	 * model.
	 * 
	 * @param message
	 *            the message to be logged
	 */
	public static void log(String message) {
		Logger.user.info(message);
	}

	/**
	 * Logs an error message.
	 * <p>
	 * Use {@link Action#logError(String)} instead of this method from inside
	 * the model.
	 * 
	 * @param message
	 *            the error message to be logged
	 */
	public static void logError(String message) {
		Logger.user.error(message);
	}

	/**
	 * Collects the given elements to a new txtUML API collection. This method
	 * creates the most general collection type, the unordered non-unique 0..*
	 * collection, {@link Any}.
	 * <p>
	 * Use {@link Action#collect(Object...)} instead of this method from inside
	 * the model.
	 * 
	 * @param elements
	 *            the elements to collect
	 * @return the newly created collection that contains the specified elements
	 */
	@SafeVarargs
	public static <E> Any<E> collect(E... elements) {
		return AbstractGeneralCollection.createAnyOf(Builder.createConsumerFor(elements));
	}

	/**
	 * Collects the given elements to a new txtUML API collection of the given
	 * type. If the specified collection type is ordered, it will contain the
	 * elements in the provided order. If it is unique and multiple equal
	 * elements are given, only the first will be contained in the collection.
	 * <p>
	 * Use {@link Action#collectIn(Class, Object...)} instead of this method
	 * from inside the model.
	 * 
	 * @param collectionType
	 *            the type of the collection to create; must be a txtUML API
	 *            collection
	 * @param elements
	 *            the elements to collect
	 * @return the newly created collection that contains the specified elements
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <E, C extends GeneralCollection<E>, C2 extends GeneralCollection<?>> C collectIn(
			Class<C2> collectionType, E... elements) {
		/*
		 * This method is declared as it is because of similar reasons why the
		 * "as" method of the GeneralCollection class is declared as that is.
		 * For details, read the comments at the "as" method.
		 */
		return (C) AbstractGeneralCollection.create(collectionType, Builder.createConsumerFor(elements));
	}

}
