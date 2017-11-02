package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.ObjectCreationError;
import hu.elte.txtuml.utils.InstanceCreator;
import hu.elte.txtuml.utils.Logger;
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
		T ret = create(classType, parameters);
		ret.runtimeInfo().setName(name);
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
		obj.runtimeInfo().delete();
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
		leftPort.getRuntime().connect(leftEnd, leftPort, rightEnd, rightPort);
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
		parentPort.getRuntime().connect(parentPort, childEnd, childPort);
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
	public static <L extends ModelClass, R extends ModelClass> void link(Class<? extends AssociationEnd<L, ?>> leftEnd,
			L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd, R rightObj) {
		leftObj.getRuntime().link(leftEnd, leftObj, rightEnd, rightObj);
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
	public static <L extends ModelClass, R extends ModelClass> void unlink(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj) {
		leftObj.getRuntime().unlink(leftEnd, leftObj, rightEnd, rightObj);
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
		obj.runtimeInfo().start();
	}

	/**
	 * Asynchronously sends the specified signal through the specified
	 * reception.
	 * <p>
	 * <b>Example:</b>
	 * </p>
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
		reception.accept(signal);
	}

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
	 *             if <code>target</code> is <code>null</code>
	 */
	@ExternalBody
	public static void send(Signal signal, ModelClass target) {
		target.runtimeInfo().send(signal);
	}

	/**
	 * Logs a message.
	 * 
	 * @param message
	 *            the message to be logged
	 */
	@ExternalBody
	public static void log(String message) {
		Logger.user.info(message);
	}

	/**
	 * Logs an error message.
	 * 
	 * @param message
	 *            the error message to be logged
	 */
	@ExternalBody
	public static void logError(String message) {
		Logger.user.error(message);
	}

}