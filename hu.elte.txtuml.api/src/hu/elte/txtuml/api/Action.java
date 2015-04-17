package hu.elte.txtuml.api;

import java.io.PrintStream;

import hu.elte.txtuml.api.backend.MultiplicityException;
import hu.elte.txtuml.api.backend.logs.ErrorMessages;
import hu.elte.txtuml.api.backend.logs.WarningMessages;
import hu.elte.txtuml.api.blocks.BlockBody;
import hu.elte.txtuml.api.blocks.Condition;
import hu.elte.txtuml.api.blocks.ParameterizedBlockBody;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * Class <code>Action</code> provides methods for the user to be used as
 * statements of the action language of the model.
 * 
 * <p>
 * <b>Represents:</b> no model element directly, static methods are part of the
 * action language
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
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public abstract class Action implements ModelElement {

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
	 * Creates a new instance of the specified model class.
	 * 
	 * @param classType
	 *            the model class for which a new instance is to be created
	 * @return a new instance of <code>classType</code> or <code>null</code> if
	 *         the creation failed
	 * @throws NullPointerException
	 *             if <code>classType</code> is <code>null</code>
	 */
	public static <T extends ModelClass> T create(Class<T> classType) {
		return InstanceCreator.createInstance(classType);
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
	 * transition. Non of the parameters should be <code>null</code>.
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
	public static <L extends ModelClass, R extends ModelClass> void link(
			Class<? extends AssociationEnd<L>> leftEnd, L leftObj,
			Class<? extends AssociationEnd<R>> rightEnd, R rightObj) {

		if (isLinkingDeleted(leftObj) || isLinkingDeleted(rightObj)) {
			return;
		}

		try {
			leftObj.addToAssoc(rightEnd, rightObj);
			rightObj.addToAssoc(leftEnd, leftObj);
		} catch (MultiplicityException e) {
			ModelExecutor.executorErrorLog(ErrorMessages
					.getUpperBoundOfMultiplicityOffendedMessage());
		}
	}

	/**
	 * Checks whether the specified model object is deleted. If it is, this
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
			ModelExecutor.executorErrorLog(ErrorMessages
					.getLinkingDeletedObjectMessage(obj));
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
	 * transition. Non of the parameters should be <code>null</code>.
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

				ModelExecutor.executorErrorLog(WarningMessages
						.getUnlinkingNonExistingAssociationMessage(leftObj,
								rightObj));

				return;
			}
		}

		leftObj.removeFromAssoc(rightEnd, rightObj);
		rightObj.removeFromAssoc(leftEnd, leftObj);
	}

	/**
	 * Checks whether the specified model object is deleted. If it is, this
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
			ModelExecutor.executorErrorLog(ErrorMessages
					.getUnlinkingDeletedObjectMessage(obj));
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
			ModelExecutor.executorErrorLog(ErrorMessages
					.getStartingDeletedObjectMessage(obj));
		}

		obj.start();
	}

	/**
	 * Asynchronously sends the specified signal to the specified target object.
	 * <p>
	 * Does not check whether the target object is deleted, it is only checked
	 * when the signal arrives to the target object.
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
	 * An <code>if</code> statement of the model. As using Java <code>if</code>
	 * statements are completely forbidden in the model, this method should be
	 * called instead.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * 
	 * <pre>
	 * <code>
	 * 	ModelInt i = new ModelInt(-1);
	 * 	Action.If( () -> i.isEqual(ModelInt.ZERO), () -> { 
	 *  		Action.log("i is zero");	
	 * 	}, () -> {
	 *  		Action.log("i is not zero");
	 * 	});
	 * </code>
	 * </pre>
	 * 
	 * @param cond
	 *            the condition of the <code>if</code> statement
	 * @param thenBody
	 *            the block to be performed if the condition is evaluated to
	 *            <code>true</code>
	 * @param elseBody
	 *            the block to be performed if the condition is evaluated to
	 *            <code>false</code>
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see ModelType
	 * @see VariableType
	 * @see Action#If(Condition, BlockBody)
	 * @see Action#While(Condition, BlockBody)
	 * @see Action#For(ModelInt, ModelInt, ParameterizedBlockBody)
	 */
	public static void If(Condition cond, BlockBody thenBody, BlockBody elseBody) {
		if (cond.check().getValue()) {
			thenBody.run();
		} else {
			elseBody.run();
		}
	}

	/**
	 * Equals to calling {@link Action#If(Condition, BlockBody, BlockBody)
	 * Action.If}<code>(cond, thenBody, () -> {})</code>.
	 * 
	 * @param cond
	 *            the condition of the <code>if</code> statement
	 * @param thenBody
	 *            the block to be performed if the condition is evaluated to
	 *            <code>true</code>
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public static void If(Condition cond, BlockBody thenBody) {
		If(cond, thenBody, () -> {
		});
	}

	/**
	 * A <code>while</code> statement of the model. As using Java
	 * <code>while</code> statements are completely forbidden in the model, this
	 * method should be called instead.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * 
	 * <pre>
	 * <code>
	 * 	VariableInt i = new VariableInt(10);
	 * 	Action.While( () -> i.get().isMore(ModelInt.ZERO), () -> { 
	 * 		Action.log("i is decreased by one");
	 * 		i.set(i.get().subtract(ModelInt.ONE));	
	 * 	});
	 * </code>
	 * </pre>
	 * 
	 * @param cond
	 *            the condition of the <code>while</code> statement
	 * @param body
	 *            the block to be performed while the condition is evaluated to
	 *            <code>true</code>
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see ModelType
	 * @see VariableType
	 * @see Action#If(Condition, BlockBody)
	 * @see Action#If(Condition, BlockBody, BlockBody)
	 * @see Action#For(ModelInt, ModelInt, ParameterizedBlockBody)
	 */
	public static void While(Condition cond, BlockBody body) {

		while (cond.check().getValue()) {
			body.run();
		}
	}

	/**
	 * A <code>foreach</code> statement of the model. As using Java
	 * <code>for</code> statements are completely forbidden in the model, this
	 * method should be called instead.
	 * <p>
	 * This method is currently <b>only available in the API</b>, its use is
	 * <b>not exported to UML2</b>.
	 * 
	 * @param collection
	 *            the collection on which this method will iterate
	 * @param body
	 *            the block to be performed on every element of the given
	 *            collection
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see ModelType
	 * @see VariableType
	 * @see Action#While(Condition, BlockBody)
	 * @see Action#If(Condition, BlockBody)
	 * @see Action#If(Condition, BlockBody, BlockBody)
	 * @see Action#For(ModelInt, ModelInt, ParameterizedBlockBody)
	 */
	public static <T extends ModelClass> void For(Collection<T> collection,
			ParameterizedBlockBody<T> body) {

		for (T element : collection) {
			body.run(element);
		}
	}

	/**
	 * A <code>for</code> statement of the model. As using Java <code>for</code>
	 * statements are completely forbidden in the model, this method should be
	 * called instead.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * 
	 * <pre>
	 * <code>
	 * Action.For( new ModelInt(1), new ModelInt(10), i -> { 
	 * 	Action.If(() -> i.isLessEqual(new ModelInt(5)), () -> {
	 * 		Action.log("showing this message five times");
	 * 	}, () -> {
	 * 		Action.log("showing this other message also five times");		  			
	 * 	});
	 * });
	 * </code>
	 * </pre>
	 * 
	 * @param begin
	 *            the first integer value to iterate on
	 * @param end
	 *            the last integer value to iterate on
	 * @param body
	 *            the block to be performed in every iteration
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see ModelType
	 * @see VariableType
	 * @see Action#While(Condition, BlockBody)
	 * @see Action#If(Condition, BlockBody)
	 * @see Action#If(Condition, BlockBody, BlockBody)
	 */
	public static void For(ModelInt begin, ModelInt end,
			ParameterizedBlockBody<ModelInt> body) {

		for (int i = begin.getValue(); i <= end.getValue(); ++i) {
			body.run(new ModelInt(i));
		}
	}

	/**
	 * Logs a message.
	 * 
	 * @param message
	 *            the message to be logged
	 * @see ModelExecutor.Settings#setUserOutStream(PrintStream)
	 */
	public static void log(String message) {
		ModelExecutor.log(message);
	}

	/**
	 * Logs an error message.
	 * 
	 * @param message
	 *            the error message to logged
	 * @see ModelExecutor.Settings#setUserErrorStream(PrintStream)
	 */
	public static void logError(String message) {
		ModelExecutor.logError(message);
	}

}