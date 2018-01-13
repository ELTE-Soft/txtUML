package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.impl.InteractionRuntime;
import hu.elte.txtuml.api.model.impl.SeqDiagThread;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public abstract class Sequence {

	/**
	 * This class is not intended to be instantiated.
	 */
	protected Sequence() {
	}

	/**
	 * Asynchronously sends the given signal to the given target from the actor;
	 * has to be called from a sequence executor thread.
	 * 
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the owner thread of the given object is not a sequence
	 *             diagram executor thread
	 */
	public static void fromActor(Signal signal, ModelClass target) {
		InteractionRuntime.current().messageFromActor(signal, target);
	}

	/**
	 * Tells the sequence diagram executor that the given signal has to be sent
	 * to the given target from the given sender.
	 */
	public static void send(ModelClass sender, Signal signal, ModelClass target) {
		InteractionRuntime.current().message(sender, signal, target);
	}

	/**
	 * Manually deactivate a lifeline. Only used when generating plantUML
	 * output.
	 * 
	 * @param lifeline
	 *            the lifeline to deactivate
	 */
	public static void deactivate(ModelClass lifeline) {
		SeqDiagThread.requirePresence();
	}

	/**
	 * Manually activate a lifeline. Only used when generating plantUML output.
	 * 
	 * @param lifeline
	 *            the lifeline to activate
	 */
	public static void activate(ModelClass lifeline) {
		SeqDiagThread.requirePresence();
	}

	/**
	 * Method used to create unnamed in-line fragments like Seq, Strict, Pars.
	 * 
	 * @param type
	 *            fragment's type
	 * @see Sequence#startFragment(CombinedFragmentType, String)
	 */
	/* public */ static void startFragment(CombinedFragmentType type) {
		// TODO fix or remove (old syntax, doesn't work (never did))

		Sequence.startFragment(type, null);
	}

	/**
	 * Method used to create in-line fragments like Seq, Strict, Pars.
	 * 
	 * @param type
	 *            fragment's type
	 * @param fragmentName
	 *            the name of the fragment
	 */
	/* public */ static void startFragment(CombinedFragmentType type, String fragmentName) {
		// TODO fix or remove (old syntax, doesn't work (never did))

		InteractionRuntime.current().startFragment(type, fragmentName);
	}

	/**
	 * Used to mark the end of in-line fragments.
	 */
	/* public */ static void endFragment() {
		// TODO fix or remove (old syntax, doesn't work (never did))

		InteractionRuntime.current().endFragment();
	}

	/**
	 * Experimental feature. Not yet exported to plantUML diagrams, only used by
	 * sequence diagram execution.
	 */
	public static void par(Interaction... operands) {
		InteractionRuntime.current().par(operands);
	}

}
