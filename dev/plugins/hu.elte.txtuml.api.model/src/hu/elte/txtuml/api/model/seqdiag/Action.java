package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public abstract class Action extends hu.elte.txtuml.api.model.Action {

	/**
	 * This class is not intended to be instantiated.
	 */
	@ExternalBody
	protected Action() {
	}

	@ExternalBody
	public static <S extends Signal> void send(ModelClass from, S signal, ModelClass target) {
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		BaseInteractionWrapper wrapper = context.getRuntime().getCurrentInteraction();
		wrapper.storeMessage(from, signal, target, false);
	}

	/**
	 * Manually deactivate a lifeline(Only used when generating plantUML output)
	 * 
	 * @param lifeline
	 *            the lifeline to deactivate
	 */
	@ExternalBody
	public static void deactivate(ModelClass lifeline) {
	}

	/**
	 * manually activate a lifeline(Only used when generating plantUML output)
	 * 
	 * @param lifeline
	 *            the lifeline to activate
	 */
	@ExternalBody
	public static void activate(ModelClass lifeline) {
	}

	/**
	 * 
	 * @param type
	 *            fragment's type
	 * @see #Action.startFragment(CombinedFragmentType, String)
	 */
	@ExternalBody
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
	@ExternalBody
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
	@ExternalBody
	public static void endFragment() {
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		context.getRuntime().fragmentModeEnded();

		context.getRuntime().getCurrentInteraction().endFragment();
	}
}
