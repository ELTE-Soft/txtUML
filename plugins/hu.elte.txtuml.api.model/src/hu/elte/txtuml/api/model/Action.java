package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.ModelExecutor.Report;
import hu.elte.txtuml.api.model.backend.MultiplicityException;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * Class <code>Action</code> provides methods for the user to be used as
 * statements of the action language.
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
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public class Action implements ModelElement {

	/**
	 * Sole constructor of <code>Action</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Package private to make sure that this class is neither instantiated, nor
	 * directly inherited by the user.
	 */
	Action() {
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
	public static <T extends ModelClass> T create(Class<T> classType,
			Object... parameters) {
		T obj = InstanceCreator.create(classType, parameters);
		if (obj == null) {
			Report.error.forEach(x -> x.modelObjectCreationFailed(classType,
					parameters));
		}
		return obj;
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
	public static void delete(ModelClass obj) {
		obj.forceDelete();
	}

	/**
	 * Links two model objects through the specified association. Shows an error
	 * message if either parameter is in {@link ModelClass.Status#DELETED
	 * DELETED} status. Has no effect if the specified association is already
	 * linked between the two objects.
	 * <p>
	 * The two specified ends must be the two different ends of the same
	 * transition. None of the parameters should be <code>null</code>.
	 * 
	 * @param <L>
	 *            the type of objects on the left end of the association
	 * @param <R>
	 *            the type of objects on the right end of the association
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
	public static <L extends ModelClass, R extends ModelClass> void link(
			Class<? extends AssociationEnd<L>> leftEnd, L leftObj,
			Class<? extends AssociationEnd<R>> rightEnd, R rightObj) {

		if (isLinkingDeleted(leftObj) || isLinkingDeleted(rightObj)) {
			return;
		}

		try {
			leftObj.addToAssoc(rightEnd, rightObj);
		} catch (MultiplicityException e) {
			Report.error.forEach(x -> x.upperBoundOfMultiplicityOffended(
					leftObj, rightEnd));
		}

		try {
			rightObj.addToAssoc(leftEnd, leftObj);
		} catch (MultiplicityException e) {
			leftObj.removeFromAssoc(rightEnd, rightObj);
			Report.error.forEach(x -> x.upperBoundOfMultiplicityOffended(
					rightObj, leftEnd));
		}
	}

	/**
	 * Checks whether the specified model object is deleted; if it is, this
	 * method shows an error about a failed linking operation because of the
	 * deleted model object given as parameter to the {@link Action#link link}
	 * method.
	 * 
	 * @param obj
	 *            the model object which's deleted status is to be checked
	 * @return <code>true</code> if the object is deleted, <code>false</code>
	 *         otherwise
	 * @throws NullPointerException
	 *             if <code>obj</code> is <code>null</code>
	 */
	private static boolean isLinkingDeleted(ModelClass obj) {
		if (obj.isDeleted()) {
			Report.error.forEach(x -> x.linkingDeletedObject(obj));
			return true;
		}
		return false;
	}

	/**
	 * Unlinks two model objects through the specified association. Shows an
	 * error message if either parameter is in {@link ModelClass.Status#DELETED
	 * DELETED} status. If the specified association is already unlinked (or was
	 * never linked) between the two objects, this method shows a warning.
	 * <p>
	 * The two specified ends must be the two different ends of the same
	 * transition. None of the parameters should be <code>null</code>.
	 * 
	 * @param <L>
	 *            the type of objects on the left end of the association
	 * @param <R>
	 *            the type of objects on the right end of the association
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
	 */
	public static <L extends ModelClass, R extends ModelClass> void unlink(
			Class<? extends AssociationEnd<L>> leftEnd, L leftObj,
			Class<? extends AssociationEnd<R>> rightEnd, R rightObj) {

		if (isUnlinkingDeleted(leftObj) || isUnlinkingDeleted(rightObj)) {
			return;
		}

		if (ModelExecutor.Settings.dynamicChecks()) {
			if (!leftObj.hasAssoc(rightEnd, rightObj)
					|| !rightObj.hasAssoc(leftEnd, leftObj)) {

				Report.warning.forEach(x -> x.unlinkingNonExistingAssociation(
						leftObj, rightObj));
				return;
			}
		}

		leftObj.removeFromAssoc(rightEnd, rightObj);
		rightObj.removeFromAssoc(leftEnd, leftObj);
	}

	/**
	 * Checks whether the specified model object is deleted; if it is, this
	 * method shows an error about a failed unlinking operation because of the
	 * deleted model object given as parameter to the {@link Action#unlink
	 * unlink} method.
	 * 
	 * @param obj
	 *            the model object which's deleted status is to be checked
	 * @return <code>true</code> if the object is deleted, <code>false</code>
	 *         otherwise
	 * @throws NullPointerException
	 *             if <code>obj</code> is <code>null</code>
	 */
	private static boolean isUnlinkingDeleted(ModelClass obj) {
		if (obj.isDeleted()) {
			Report.error.forEach(x -> x.unlinkingDeletedObject(obj));
			return true;
		}
		return false;
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
	public static void start(ModelClass obj) {
		if (obj.isDeleted()) {
			Report.error.forEach(x -> x.startingDeletedObject(obj));
		}

		obj.start();
	}

	/**
	 * Asynchronously sends the specified signal to the specified target object.
	 * <p>
	 * Does not check whether the target object is deleted, it is only checked
	 * when the signal arrives to the object.
	 * 
	 * @param target
	 *            the model object which will receive the signal
	 * @param signal
	 *            the signal object to send
	 * @throws NullPointerException
	 *             if <code>target</code> is <code>null</code>
	 */
	public static void send(ModelClass target, Signal signal) {
		target.send(signal);
	}

	/**
	 * Logs a message.
	 * 
	 * @param message
	 *            the message to be logged
	 * @see ModelExecutor.Settings#setUserOutStream(java.io.PrintStream)
	 */
	public static void log(String message) {
		ModelExecutor.userLog(message);
	}

	/**
	 * Logs an error message.
	 * 
	 * @param message
	 *            the error message to logged
	 * @see ModelExecutor.Settings#setUserErrorStream(java.io.PrintStream)
	 */
	public static void logError(String message) {
		ModelExecutor.userErrorLog(message);
	}

}