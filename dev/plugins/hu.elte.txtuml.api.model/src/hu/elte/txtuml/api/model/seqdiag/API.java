package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public abstract class API {
	public static void send(Signal signal, ModelClass target) {
		hu.elte.txtuml.api.model.API.send(signal, target);
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		InteractionWrapper wrapper = context.getInteractionWrapper();
		wrapper.storeMessage(null, signal, target, true);
	}
}
