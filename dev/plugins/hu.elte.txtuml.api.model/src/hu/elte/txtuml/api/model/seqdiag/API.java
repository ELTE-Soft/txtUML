package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.impl.ExecutorThread;
import hu.elte.txtuml.api.model.impl.seqdiag.SeqDiagExecutorThread;
import hu.elte.txtuml.api.model.impl.seqdiag.SeqDiagRuntime;

public abstract class API extends hu.elte.txtuml.api.model.API {

	/**
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the owner thread of the given object is not a sequence
	 *             diagram executor thread
	 */
	@ExternalBody
	public static void send(Signal signal, ModelClass target) {
		hu.elte.txtuml.api.model.API.send(signal, target);

		ExecutorThread thread = RuntimeProvider.INSTANCE.getRuntimeOf(target).getThread();

		SeqDiagRuntime runtime = SeqDiagExecutorThread.cast(thread).getSeqDiagRuntime();

		runtime.getCurrentInteraction().storeActorMessage(signal, target);
	}
}
