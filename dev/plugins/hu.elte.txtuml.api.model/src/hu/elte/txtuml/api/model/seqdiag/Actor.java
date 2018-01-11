package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.impl.SeqDiagExecutorThread;

public abstract class Actor {

	/**
	 * This class is not intended to be instantiated.
	 */
	@ExternalBody
	protected Actor() {
	}

	/**
	 * Asynchronously sends the given signal to the given target from the actor;
	 * has to be called from a sequence executor thread.
	 * 
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the owner thread of the given object is not a sequence
	 *             diagram executor thread
	 */
	@ExternalBody
	public static void send(Signal signal, ModelClass target) {
		SeqDiagExecutorThread.current().getSeqDiagRuntime().storeMessageFromActor(signal, target);

		hu.elte.txtuml.api.model.Action.send(signal, target);
	}
}
