package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;

public abstract class Action {

	public static <T extends ModelClass> T create(Class<T> classType, Object... parameters) {
		return hu.elte.txtuml.api.model.Action.create(classType, parameters);
	}

	public static void delete(ModelClass obj) {
		hu.elte.txtuml.api.model.Action.delete(obj);
	}

	public static <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort) {
		hu.elte.txtuml.api.model.Action.connect(leftEnd, leftPort, rightEnd, rightPort);
	}

	public static <P1 extends Port<I1, I2>, C extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C> childEnd, P2 childPort) {
		hu.elte.txtuml.api.model.Action.connect(parentPort, childEnd, childPort);
	}

	public static <L extends ModelClass, R extends ModelClass> void link(Class<? extends AssociationEnd<L, ?>> leftEnd,
			L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd, R rightObj) {
		hu.elte.txtuml.api.model.Action.link(leftEnd, leftObj, rightEnd, rightObj);
	}

	public static <L extends ModelClass, R extends ModelClass> void unlink(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj) {
		hu.elte.txtuml.api.model.Action.unlink(leftEnd, leftObj, rightEnd, rightObj);
	}

	public static void start(ModelClass obj) {
		hu.elte.txtuml.api.model.Action.start(obj);
	}

	public static <S extends Signal> void send(ModelClass from, S signal, ModelClass target) {
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		BaseInteractionWrapper wrapper = context.getRuntime().getCurrentInteraction();
		wrapper.storeMessage(from, signal, target);
	}

	public static void log(String message) {
		hu.elte.txtuml.api.model.Action.log(message);
	}

	public static void logError(String message) {
		hu.elte.txtuml.api.model.Action.logError(message);
	}

	/**
	 * Manually deactivate a lifeline(Only used when generating plantUML output)
	 * 
	 * @param lifeline
	 *            the lifeline to deactivate
	 */
	public static void deactivate(ModelClass lifeline) {

	}

	/**
	 * manually activate a lifeline(Only used when generating plantUML output)
	 * 
	 * @param lifeline
	 *            the lifeline to activate
	 */
	public static void activate(ModelClass lifeline) {
	}

	/**
	 * 
	 * @param type
	 *            fragment's type
	 * @see #Action.startFragment(CombinedFragmentType, String)
	 */
	public static void startFragment(CombinedFragmentType type) {
		Action.startFragment(type, null);
	}

	/**
	 * Method used to create in-line fragments like Seq,Strict,Pars
	 * 
	 * @param type
	 *            fragment's type
	 * @param fragmentName
	 *            the name of the fragment
	 */
	public static void startFragment(CombinedFragmentType type, String fragmentName) {
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		context.getRuntime().setFragmentMode(type);
		if (fragmentName == null) {
			fragmentName = "UnnamedFragment";
		}
		context.getRuntime().getCurrentInteraction().storeFragment(type, fragmentName);
	}

	/**
	 * Used to mark the end of in-line fragmentss
	 */
	public static void endFragment() {
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		context.getRuntime().fragmentModeEnded();

		context.getRuntime().getCurrentInteraction().endFragment();
	}
}
