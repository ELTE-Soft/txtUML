package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.api.model.execution.seqdiag.error.LostMessageError;
import hu.elte.txtuml.api.model.execution.seqdiag.error.MessageToDeletedError;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Reports errors when messages are lost or arrive to deleted model objects.
 */
@SequenceDiagramRelated
class InvalidMessageSentListener implements WarningListener {
	private final DefaultSeqDiagExecutor executor;

	public InvalidMessageSentListener(DefaultSeqDiagExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void lostSignalAtObject(Signal signal, ModelClass obj) {
		executor.addError(new LostMessageError(Message.fromUnknown(signal, obj)));
	}

	@Override
	public void signalArrivedToDeletedObject(ModelClass obj, Signal signal) {
		executor.addError(new MessageToDeletedError(Message.fromUnknown(signal, obj)));
	}

	@Override
	public void lostSignalAtPort(Signal signal, Port<?, ?> portInstance) {
		// portError
	}
}
