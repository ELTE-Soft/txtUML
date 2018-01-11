package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.impl.SeqDiagExecutorThread;

public abstract class Action extends hu.elte.txtuml.api.model.Action {

	/**
	 * This class is not intended to be instantiated.
	 */
	@ExternalBody
	protected Action() {
	}

	/**
	 * Tells the sequence diagram executor that the given signal has to be sent
	 * to the given target from the given sender.
	 */
	@ExternalBody
	public static void send(Signal signal, ModelClass target, ModelClass sender) {
		SeqDiagExecutorThread.current().getSeqDiagRuntime().storeMessage(signal, target, sender);
	}

	/**
	 * Manually deactivate a lifeline. Only used when generating plantUML
	 * output.
	 * 
	 * @param lifeline
	 *            the lifeline to deactivate
	 */
	@ExternalBody
	public static void deactivate(ModelClass lifeline) {
		SeqDiagExecutorThread.requirePresence();
	}

	/**
	 * Manually activate a lifeline. Only used when generating plantUML output.
	 * 
	 * @param lifeline
	 *            the lifeline to activate
	 */
	@ExternalBody
	public static void activate(ModelClass lifeline) {
		SeqDiagExecutorThread.requirePresence();
	}

	/**
	 * Method used to create unnamed in-line fragments like Seq, Strict, Pars.
	 * 
	 * @param type
	 *            fragment's type
	 * @see Action#startFragment(CombinedFragmentType, String)
	 */
	@ExternalBody
	public static void startFragment(CombinedFragmentType type) {
		Action.startFragment(type, null);
	}

	/**
	 * Method used to create in-line fragments like Seq, Strict, Pars.
	 * 
	 * @param type
	 *            fragment's type
	 * @param fragmentName
	 *            the name of the fragment
	 */
	@ExternalBody
	public static void startFragment(CombinedFragmentType type, String fragmentName) {
		SeqDiagExecutorThread.current().getSeqDiagRuntime().storeFragment(type, fragmentName);
	}

	/**
	 * Used to mark the end of in-line fragments.
	 */
	@ExternalBody
	public static void endFragment() {
		SeqDiagExecutorThread.current().getSeqDiagRuntime().endFragment();
	}
}
