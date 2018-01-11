package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.AbstractGeneralCollection.Builder;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.ObjectCreationError;
import hu.elte.txtuml.api.model.impl.ExecutorThread;
import hu.elte.txtuml.utils.InstanceCreator;
import hu.elte.txtuml.utils.RuntimeInvocationTargetException;

/**
 * Provides static methods for the user to be used as statements of the action
 * language. Its methods may only <b>be called from the model</b>, see the class
 * {@link API} for possible ways to communicate with the model from the outside.
 * 
 * <p>
 * <b>Represents:</b> no model element directly, its static methods are part of
 * the action language
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Call its static methods from any method of the model where action language
 * should be used.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed, for simplified access to its methods</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class Action {

	/**
	 * This class is not intended to be instantiated.
	 */
	@ExternalBody
	protected Action() {
	}

	/**
	 * Creates a new instance of the specified model class. Shows an error
	 * message if the creation failed for any reason.
	 * 
	 * @param <T>
	 *            the type of the new model object
	 * @param classType
	 *            the model class for which a new instance is to be created
	 * @param parameters
	 *            parameters of the new object
	 * @return a new instance of <code>classType</code> or <code>null</code> if
	 *         the creation failed
	 * @throws NullPointerException
	 *             if <code>classType</code> is <code>null</code>
	 */
	@ExternalBody
	public static <T extends ModelClass> T create(Class<T> classType, Object... parameters) throws ObjectCreationError {
		ExecutorThread.requirePresence();

		try {
			return InstanceCreator.create(classType, parameters);
		} catch (IllegalArgumentException | RuntimeInvocationTargetException e) {
			throw new ObjectCreationError(e);
		}
	}

	/**
	 * Creates a new instance of the specified model class and sets its name.
	 * Shows an error message if the creation failed for any reason.
	 * <p>
	 * <b>Note:</b> The {@link ModelClass#toString} method returns the name of
	 * the instance if it has been set. Therefore this name will be the string
	 * representation of the created object in any report, unless its
	 * {@code toString} method is overridden.
	 * 
	 * @param <T>
	 *            the type of the new model object
	 * @param classType
	 *            the model class for which a new instance is to be created
	 * @param name
	 *            the name of the created object
	 * @param parameters
	 *            parameters of the new object
	 * @return a new instance of <code>classType</code> or <code>null</code> if
	 *         the creation failed
	 * @throws NullPointerException
	 *             if <code>classType</code> is <code>null</code>
	 */
	@ExternalBody
	public static <T extends ModelClass> T createWithName(Class<T> classType, String name, Object... parameters)
			throws ObjectCreationError {
		T ret = Action.create(classType, parameters);
		ret.runtime().setName(name);
		return ret;
	}

	/**
	 * Deletes the specified model object. Might only be called if all
	 * associations of the specified model object are already unlinked. Shows an
	 * error otherwise.
	 * <p>
	 * See {@link ModelClass.Status#DELETED DELETED} status of model objects for
	 * more information about model object deletion.
	 * 
	 * @param obj
	 *            the model object to be deleted
	 * @throws NullPointerException
	 *             if <code>obj</code> is <code>null</code>
	 */
	@ExternalBody
	public static void delete(ModelClass obj) {
		ExecutorThread.current().requireOwned(obj);

		obj.runtime().delete();
	}

	/**
	 * Connects two ports through the specified assembly connector.
	 * <p>
	 * The two specified ends must be the two different ends of the same
	 * connector. None of the parameters should be <code>null</code>.
	 * 
	 * @param leftEnd
	 *            the left end of the connector
	 * @param leftPort
	 *            the port instance at the left end of the connector
	 * @param rightEnd
	 *            the right end of the connector
	 * @param rightPort
	 *            the port instance at the right end of the connector
	 * @throws NullPointerException
	 *             if either <code>leftPort</code> or <code>rightPort</code> is
	 *             <code>null</code>
	 */
	@ExternalBody
	public static <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort) {
		ExecutorThread exec = ExecutorThread.current();
		exec.requireOwned(leftPort);
		exec.requireOwned(rightPort);

		exec.getModelRuntime().connect(leftEnd, leftPort, rightEnd, rightPort);
	}

	/**
	 * Connects two ports through the specified delegation connector.
	 * <p>
	 * None of the parameters should be <code>null</code>.
	 * 
	 * @param parentPort
	 *            the port instance of the container object
	 * @param childEnd
	 *            the end at the contained object
	 * @param childPort
	 *            the port instance of the contained object
	 * @throws NullPointerException
	 *             if either <code>leftPort</code> or <code>rightPort</code> is
	 *             <code>null</code>
	 */
	@ExternalBody
	public static <P1 extends Port<I1, I2>, C extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C> childEnd, P2 childPort) {
		ExecutorThread exec = ExecutorThread.current();
		exec.requireOwned(parentPort);
		exec.requireOwned(childPort);

		exec.getModelRuntime().connect(parentPort, childEnd, childPort);
	}

	/**
	 * Links two model objects through the specified association. Shows an error
	 * message if either parameter is in {@link ModelClass.Status#DELETED
	 * DELETED} status. Has no effect if the specified association is already
	 * linked between the two objects.
	 * <p>
	 * The two specified ends must be the two different ends of the same
	 * association. None of the parameters should be <code>null</code>.
	 * 
	 * @param leftEnd
	 *            the left end of the association
	 * @param leftObj
	 *            the object at the left end of the association
	 * @param rightEnd
	 *            right end of the association
	 * @param rightObj
	 *            the object at the right end of the association
	 * @throws NullPointerException
	 *             if either <code>leftEnd</code> or <code>rightEnd</code> is
	 *             <code>null</code>
	 * @see Association
	 * @see AssociationEnd
	 * @see ModelClass.Status#DELETED
	 */
	@ExternalBody
	public static <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void link(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj) {
		ExecutorThread exec = ExecutorThread.current();
		exec.requireOwned(leftObj);
		exec.requireOwned(rightObj);

		exec.getModelRuntime().link(leftEnd, leftObj, rightEnd, rightObj);
	}

	/**
	 * Unlinks two model objects through the specified association. Shows an
	 * error message if either parameter is in {@link ModelClass.Status#DELETED
	 * DELETED} status. If the specified association is already unlinked (or was
	 * never linked) between the two objects, this method shows a warning.
	 * <p>
	 * The two specified ends must be the two different ends of the same
	 * association. None of the parameters should be <code>null</code>.
	 * 
	 * @param leftEnd
	 *            the left end of the association
	 * @param leftObj
	 *            the object at the left end of the association
	 * @param rightEnd
	 *            right end of the association
	 * @param rightObj
	 *            the object at the right end of the association
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see Association
	 * @see AssociationEnd
	 */
	@ExternalBody
	public static <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void unlink(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj) {
		ExecutorThread exec = ExecutorThread.current();
		exec.requireOwned(leftObj);
		exec.requireOwned(rightObj);

		exec.getModelRuntime().unlink(leftEnd, leftObj, rightEnd, rightObj);
	}

	/**
	 * Starts the state machine of the specified model object. Shows an error
	 * message if the parameter is in {@link ModelClass.Status#DELETED DELETED}
	 * status.
	 * 
	 * @param obj
	 *            the model object which's state machine has to be started
	 * @throws NullPointerException
	 *             if <code>obj</code> is <code>null</code>
	 */
	@ExternalBody
	public static void start(ModelClass obj) {
		ExecutorThread.current().requireOwned(obj);

		obj.runtime().start();
	}

	/**
	 * Asynchronously sends the specified signal through the specified
	 * reception.
	 * <p>
	 * <b>Example:</b>
	 * 
	 * <pre>
	 * <code>
	 * Action.send(new MySignal(), port(MyPort.class).required::reception);
	 * </code>
	 * </pre>
	 * 
	 * @param signal
	 *            the signal object to send
	 * @param reception
	 *            the reception which will accept the signal
	 * @throws NullPointerException
	 *             if <code>reception</code> is <code>null</code>
	 */
	@ExternalBody
	public static <S extends Signal> void send(S signal, Reception<S> reception) {
		ExecutorThread.requirePresence();

		reception.accept(signal);
	}

	/**
	 * Asynchronously sends the specified signal to the specified target object.
	 * <p>
	 * Does not check whether the target object is deleted, it is only checked
	 * when the signal arrives to the object.
	 * <p>
	 * Use {@link API#send(Signal, ModelClass)} instead of this method from
	 * outside the model.
	 * 
	 * @param signal
	 *            the signal object to send
	 * @param target
	 *            the model object which will receive the signal
	 * 
	 * @throws NullPointerException
	 *             if <code>target</code> is <code>null</code>
	 */
	@ExternalBody
	public static void send(Signal signal, ModelClass target) {
		ExecutorThread.requirePresence();

		API.send(signal, target);
	}

	/**
	 * Asynchronously sends the specified signal to the specified target object.
	 * <p>
	 * Does not check whether the target object is deleted, it is only checked
	 * when the signal arrives to the object.
	 * 
	 *
	 * @param signal
	 *            the signal object to send
	 * @param target
	 *            the model object which will receive the signal
	 * @param sender
	 *            the sender model object( optional )
	 * 
	 * @throws NullPointerException
	 *             if <code>target</code> is <code>null</code>
	 */
	@ExternalBody
	public static void send(Signal signal, ModelClass target, ModelClass sender) {
		ExecutorThread.current().requireOwned(sender);

		target.runtime().receiveLater(signal, sender);
	}

	/**
	 * Logs a message.
	 * <p>
	 * Use {@link API#log(String)} instead of this method from outside the
	 * model.
	 * 
	 * @param message
	 *            the message to be logged
	 */
	@ExternalBody
	public static void log(String message) {
		ExecutorThread.requirePresence();

		API.log(message);
	}

	/**
	 * Logs an error message.
	 * <p>
	 * Use {@link API#logError(String)} instead of this method from outside the
	 * model.
	 * 
	 * @param message
	 *            the error message to be logged
	 */
	@ExternalBody
	public static void logError(String message) {
		ExecutorThread.requirePresence();

		API.logError(message);
	}

	/**
	 * Collects the given elements to a new txtUML API collection. This method
	 * creates the most general collection type, the unordered non-unique 0..*
	 * collection, {@link Any}.
	 * <p>
	 * Use {@link API#collect(Object...)} instead of this method from outside
	 * the model.
	 * 
	 * @param elements
	 *            the elements to collect
	 * @return the newly created collection that contains the specified elements
	 */
	@ExternalBody
	@SafeVarargs
	public static <E> Any<E> collect(E... elements) {
		ExecutorThread.requirePresence();

		return AbstractGeneralCollection.createAnyOf(Builder.createConsumerFor(elements));
	}

	/**
	 * Collects the given elements to a new txtUML API collection of the given
	 * type. If the specified collection type is ordered, it will contain the
	 * elements in the provided order. If it is unique and multiple equal
	 * elements are given, only the first will be contained in the collection.
	 * <p>
	 * Use {@link API#collectIn(Class, Object...)} instead of this method from
	 * outside the model.
	 * 
	 * @param collectionType
	 *            the type of the collection to create; must be a txtUML API
	 *            collection
	 * @param elements
	 *            the elements to collect
	 * @return the newly created collection that contains the specified elements
	 */
	@ExternalBody
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <E, C extends GeneralCollection<E>, C2 extends GeneralCollection<?>> C collectIn(
			Class<C2> collectionType, E... elements) {
		/*
		 * This method is declared as it is because of similar reasons why the
		 * "as" method of the GeneralCollection class is declared as that is.
		 * For details, read the comments at the "as" method.
		 */
		ExecutorThread.requirePresence();

		return (C) AbstractGeneralCollection.create(collectionType, Builder.createConsumerFor(elements));
	}

}