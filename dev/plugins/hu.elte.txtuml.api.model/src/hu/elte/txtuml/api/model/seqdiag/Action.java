package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.impl.seqdiag.SeqDiagExecutorThread;
import hu.elte.txtuml.api.model.impl.seqdiag.SeqDiagRuntime;

public abstract class Action extends hu.elte.txtuml.api.model.Action {

	/**
	 * This class is not intended to be instantiated.
	 */
	@ExternalBody
	protected Action() {
	}

	@ExternalBody
	public static void send(Signal signal, ModelClass target, ModelClass sender) {
		SeqDiagRuntime runtime = SeqDiagExecutorThread.current().getSeqDiagRuntime();
		runtime.getCurrentInteraction().storeMessage(signal, target, sender);
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
		SeqDiagRuntime runtime = SeqDiagExecutorThread.current().getSeqDiagRuntime();
		runtime.setFragmentMode(type);
		if (fragmentName == null) {
			fragmentName = "UnnamedFragment";
		}
		runtime.getCurrentInteraction().storeFragment(type, fragmentName);
	}

	/**
	 * Used to mark the end of in-line fragments.
	 */
	@ExternalBody
	public static void endFragment() {		
		SeqDiagRuntime runtime = SeqDiagExecutorThread.current().getSeqDiagRuntime();
		runtime.fragmentModeEnded();

		runtime.getCurrentInteraction().endFragment();
	}
}
