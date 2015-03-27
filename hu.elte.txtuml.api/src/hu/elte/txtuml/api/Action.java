package hu.elte.txtuml.api;

import hu.elte.txtuml.api.blocks.BlockBody;
import hu.elte.txtuml.api.blocks.Condition;
import hu.elte.txtuml.api.blocks.ParameterizedBlockBody;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * Class <code>Action</code> provides methods for the user to be used as
 * statements of the action language of the model.
 * 
 * <p>
 * <b>Represents:</b> no model element directly
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed
 * <li><i>Define subtype:</i> disallowed
 * </ul>
 * </li> </ul>
 * 
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * <pre>
 * <code>
 * </code>
 * </pre>
 * 
 * TODO See the documentation of the {@link hu.elte.txtuml.api} package to get
 * an overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
 *
 */
public abstract class Action implements ModelElement {

	/**
	 * Sole constructor of <code>Action</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Package private to make sure that this class is not instantiated, nor
	 * directly inherited by the user.
	 */
	Action() {
	}

	public static <T extends ModelClass> T create(Class<T> classType) {
		return InstanceCreator.createInstance(classType);
	}

	public static void delete(ModelClass obj) {
		// TODO implement
	}

	public static <MODELCLASS1 extends ModelClass, MODELCLASS2 extends ModelClass> void link(
			Class<? extends AssociationEnd<MODELCLASS1>> leftEnd,
			MODELCLASS1 leftObj,
			Class<? extends AssociationEnd<MODELCLASS2>> rightEnd,
			MODELCLASS2 rightObj) {

		leftObj.addToAssoc(rightEnd, rightObj);
		rightObj.addToAssoc(leftEnd, leftObj);
	}

	/**
	 * Starts the state machine of the specified modelClass object. TODO
	 * 
	 * @param obj
	 *            the model object the state machine of which has to be started.
	 * @throws NullPointerException
	 *             if <code>obj</code> is <code>null</code>
	 */
	public static void start(ModelClass obj) {
		obj.start();
	}

	/**
	 * Asynchronously sends the specified signal to the specified target object.
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
	 * An <code>if</code> statement of the model
	 * 
	 * TODO
	 * 
	 * @param cond
	 * @param thenBody
	 * @param elseBody
	 */
	public static void If(Condition cond, BlockBody thenBody, BlockBody elseBody) {

		if (cond.check().getValue()) {
			thenBody.run();
		} else {
			elseBody.run();
		}
	}

	public static void If(Condition cond, BlockBody thenBody) {
		If(cond, thenBody, () -> {
		});
	}

	public static void While(Condition cond, BlockBody body) {

		while (cond.check().getValue()) {
			body.run();
		}
	}

	public static <T extends ModelClass> void For(Collection<T> collection,
			ParameterizedBlockBody<T> body) {
		// TODO import 'For' (foreach) into UML2

		for (T element : collection) {
			body.run(element);
		}
	}

	public static void For(ModelInt begin, ModelInt end,
			ParameterizedBlockBody<ModelInt> body) {

		for (int i = begin.getValue(); i <= end.getValue(); ++i) {
			body.run(new ModelInt(i));
		}
	}

	public static void log(String message) { // user log
		ModelExecutor.log(message);
	}

	public static void logError(String message) { // user log
		ModelExecutor.logError(message);
	}

	static void executorLog(String message) { // api log
		ModelExecutor.executorLog(message);
	}

	static void executorFormattedLog(String format, Object... args) { // api log
		ModelExecutor.executorFormattedLog(format, args);
	}

	static void executorErrorLog(String message) { // api log
		ModelExecutor.executorErrorLog(message);
	}
}